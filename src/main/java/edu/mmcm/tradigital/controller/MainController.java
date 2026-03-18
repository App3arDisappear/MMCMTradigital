package edu.mmcm.tradigital.controller;

import edu.mmcm.tradigital.model.Order;
import edu.mmcm.tradigital.model.Product;
import edu.mmcm.tradigital.repo.OrderRepository;
import edu.mmcm.tradigital.service.ProductService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainController {

    @FXML private TableView<Product> tblCatalog;
    @FXML private TableColumn<Product, String> colSku, colName, colCategory;
    @FXML private TableColumn<Product, Double> colPrice;
    @FXML private TableColumn<Product, Integer> colStock;
    @FXML private TextField searchField;
    @FXML private Spinner<Integer> spnQty;
    @FXML private Label lblCartTotal;
    @FXML private Button btnAdmin;

    @FXML private Label lblItemDescription;

    private ProductService productService;
    private OrderRepository orderRepository;
    private List<Product> cart = new ArrayList<>();
    private double totalAmount = 0.0;
    private boolean isEasyMode = false;

    @FXML
    public void initialize() {
        productService = new ProductService();
        orderRepository = new OrderRepository();

        colSku.setCellValueFactory(new PropertyValueFactory<>("sku"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("categoryId"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stockQuantity"));

        if (spnQty != null) {
            spnQty.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));
        }

        tblCatalog.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            updateSmartGuide(newSelection);
        });

        refreshTable();
    }

    private void updateSmartGuide(Product p) {
        if (p == null) {
            lblItemDescription.setText("Click an item in the table to see details here.");
            return;
        }

        if (!isEasyMode) {
            lblItemDescription.setText("Selected: " + p.getName() + " | Category: " + p.getCategoryId());
        } else {
            String code = p.getSku();
            String letters = code.replaceAll("[^A-Za-z]", "");
            String numbers = code.replaceAll("[^0-9]", "");
            String category = p.getCategoryId() != null ? p.getCategoryId().toLowerCase() : "";

            StringBuilder guide = new StringBuilder();

            // Format horizontally so JavaFX doesn't cut off the text
            guide.append("💡 EASY MODE [").append(code).append("] ➔ ");

            // --- YOUR SPECIFIC BUSINESS RULES ---
            if (category.contains("uniform") || category.contains("clothes")) {
                if (!letters.isEmpty()) {
                    guide.append("'").append(letters).append("' = Uniform Type");
                }
                if (numbers.length() >= 2) {
                    guide.append(" | 1st Digit '").append(numbers.charAt(0)).append("' = Size");
                    guide.append(" | 2nd Digit '").append(numbers.charAt(1)).append("' = Color");
                } else if (!numbers.isEmpty()) {
                    guide.append(" | Digit '").append(numbers).append("' = Size/Color");
                }
            } else {
                if (!letters.isEmpty()) {
                    guide.append("'").append(letters).append("' = Product Type");
                }
                if (!numbers.isEmpty()) {
                    guide.append(" | Digit '").append(numbers).append("' = Color");
                }
            }

            lblItemDescription.setText(guide.toString());
        }
    }

    public void setUserRole(boolean isAdmin) {
        if (btnAdmin != null) {
            btnAdmin.setVisible(isAdmin);
            btnAdmin.setManaged(isAdmin);
        }
    }

    private void refreshTable() {
        try {
            Product savedSelection = tblCatalog.getSelectionModel().getSelectedItem();
            List<Product> products = productService.getAllProducts();
            tblCatalog.setItems(FXCollections.observableArrayList(products));

            if (savedSelection != null) {
                for (Product p : products) {
                    if (p.getSku().equals(savedSelection.getSku())) {
                        tblCatalog.getSelectionModel().select(p);
                        break;
                    }
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    public void addToCart(ActionEvent event) {
        Product selected = tblCatalog.getSelectionModel().getSelectedItem();
        if (selected != null) {
            int qty = (spnQty != null) ? spnQty.getValue() : 1;

            long currentInCart = cart.stream().filter(p -> p.getSku().equals(selected.getSku())).count();
            if (currentInCart + qty > selected.getStockQuantity()) {
                showAlert("Stock Error", "Not enough stock available for this item!", Alert.AlertType.WARNING);
                return;
            }

            for (int i = 0; i < qty; i++) { cart.add(selected); }
            totalAmount += (selected.getPrice() * qty);
            lblCartTotal.setText("₱" + String.format("%.2f", totalAmount));
            showAlert("Cart Updated", "Added " + qty + "x " + selected.getName() + " to your cart.", Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    public void checkout(ActionEvent event) {
        Stage stage = (Stage) tblCatalog.getScene().getWindow();
        if (cart.isEmpty()) {
            showAlert("Thank You", "Thank you for visiting! Closing application...", Alert.AlertType.INFORMATION);
            stage.close();
            return;
        }

        Map<String, Long> summaryItems = cart.stream().collect(Collectors.groupingBy(Product::getName, Collectors.counting()));
        StringBuilder cartSummary = new StringBuilder("Please review your items:\n\n");
        for (Map.Entry<String, Long> entry : summaryItems.entrySet()) {
            cartSummary.append(entry.getValue()).append("x  ").append(entry.getKey()).append("\n");
        }
        cartSummary.append("\nTotal to pay: ₱").append(String.format("%.2f", totalAmount));

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Checkout");
        confirmAlert.setHeaderText("Your Shopping Cart");
        confirmAlert.setContentText(cartSummary.toString());

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                Order newOrder = new Order("Customer", new ArrayList<>(cart), totalAmount);
                orderRepository.saveOrder(newOrder);

                Map<String, Long> skuQuantities = cart.stream().collect(Collectors.groupingBy(Product::getSku, Collectors.counting()));
                for (Map.Entry<String, Long> entry : skuQuantities.entrySet()) {
                    Product p = productService.getAllProducts().stream().filter(prod -> prod.getSku().equals(entry.getKey())).findFirst().orElse(null);
                    if (p != null) {
                        p.setStockQuantity(p.getStockQuantity() - entry.getValue().intValue());
                        productService.updateProduct(p);
                    }
                }

                showAlert("Success", "Order " + newOrder.getOrderId() + " placed successfully!", Alert.AlertType.INFORMATION);
                cart.clear();
                totalAmount = 0.0;
                lblCartTotal.setText("₱0.00");
                refreshTable();
            }
        });
    }

    @FXML
    public void removeSelectedLine(ActionEvent event) {
        Product selected = tblCatalog.getSelectionModel().getSelectedItem();
        if (selected != null) {
            cart.removeIf(p -> p.getSku().equals(selected.getSku()));
            totalAmount = 0;
            for (Product p : cart) { totalAmount += p.getPrice(); }
            lblCartTotal.setText("₱" + String.format("%.2f", totalAmount));
            showAlert("Item Removed", "Removed " + selected.getName() + " from cart.", Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    public void search(ActionEvent event) {
        Product savedSelection = tblCatalog.getSelectionModel().getSelectedItem();
        String query = searchField.getText().toLowerCase();
        List<Product> filtered = productService.getAllProducts().stream()
                .filter(p -> p.getName().toLowerCase().contains(query) || p.getSku().toLowerCase().contains(query))
                .toList();
        tblCatalog.setItems(FXCollections.observableArrayList(filtered));

        if (savedSelection != null) {
            for (Product p : filtered) {
                if (p.getSku().equals(savedSelection.getSku())) {
                    tblCatalog.getSelectionModel().select(p);
                    break;
                }
            }
        }
    }

    @FXML
    public void toggleEasyMode(ActionEvent event) {
        isEasyMode = !isEasyMode;
        Product selected = tblCatalog.getSelectionModel().getSelectedItem();
        updateSmartGuide(selected);
        tblCatalog.setStyle(isEasyMode ? "-fx-font-size: 16px;" : "");
    }

    @FXML
    public void openAdmin(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/mmcm/tradigital/fxml/admin_dashboard.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Admin Panel");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
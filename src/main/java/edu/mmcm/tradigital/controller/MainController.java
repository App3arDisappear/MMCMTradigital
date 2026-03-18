package edu.mmcm.tradigital.controller;

import edu.mmcm.tradigital.model.Product;
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

public class MainController {

    @FXML private TableView<Product> tblCatalog;
    @FXML private TableColumn<Product, String> colSku, colName, colCategory;
    @FXML private TableColumn<Product, Double> colPrice;
    @FXML private TableColumn<Product, Integer> colStock;
    @FXML private TextField searchField;
    @FXML private Spinner<Integer> spnQty;
    @FXML private Label lblCartTotal;

    private ProductService productService;
    private List<Product> cart = new ArrayList<>();
    private double totalAmount = 0.0;

    @FXML
    public void initialize() {
        productService = new ProductService();
        colSku.setCellValueFactory(new PropertyValueFactory<>("sku"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("categoryId"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stockQuantity"));

        if (spnQty != null) {
            spnQty.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));
        }
        refreshTable();
    }

    private void refreshTable() {
        try {
            tblCatalog.setItems(FXCollections.observableArrayList(productService.getAllProducts()));
        } catch (Exception e) {
            System.out.println("No DB connection.");
        }
    }

    @FXML
    public void addToCart(ActionEvent event) {
        Product selected = tblCatalog.getSelectionModel().getSelectedItem();
        if (selected != null) {
            int qty = (spnQty != null) ? spnQty.getValue() : 1;
            for (int i = 0; i < qty; i++) { cart.add(selected); }
            totalAmount += (selected.getPrice() * qty);
            lblCartTotal.setText("₱" + String.format("%.2f", totalAmount));
            showAlert("Success", selected.getName() + " added!", Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    public void checkout(ActionEvent event) {
        Stage stage = (Stage) tblCatalog.getScene().getWindow();
        if (cart.isEmpty()) {
            // SUCCESS: Guest leaves without buying (Window Shopping)
            showAlert("Goodbye", "Thank you for visiting! Come back soon.", Alert.AlertType.INFORMATION);
            stage.close();
        } else {
            // SUCCESS: Purchase completed
            showAlert("Order Placed", "Total: ₱" + String.format("%.2f", totalAmount), Alert.AlertType.INFORMATION);
            stage.close();
        }
    }

    @FXML
    public void openAdmin(ActionEvent event) {
        try {
            // This path MUST match your folder structure exactly
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/mmcm/tradigital/fxml/admin_dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Admin Control Panel");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Could not open Admin. Verify 'admin_dashboard.fxml' is in the resources/edu/mmcm/tradigital/fxml folder.", Alert.AlertType.ERROR);
        }
    }

    @FXML public void search(ActionEvent event) { refreshTable(); /* Basic refresh for now */ }
    @FXML public void toggleEasyMode(ActionEvent event) { tblCatalog.setStyle("-fx-font-size: 18px;"); }
    @FXML public void removeSelectedLine(ActionEvent event) { cart.clear(); totalAmount = 0; lblCartTotal.setText("₱0.00"); }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
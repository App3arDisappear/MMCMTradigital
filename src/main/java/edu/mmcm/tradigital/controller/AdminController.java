package edu.mmcm.tradigital.controller;

import edu.mmcm.tradigital.model.Category;
import edu.mmcm.tradigital.model.Product;
import edu.mmcm.tradigital.service.CategoryService;
import edu.mmcm.tradigital.service.ProductService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class AdminController {

    @FXML private TableView<Product> tblProducts;
    @FXML private TableColumn<Product, String> colProdSku, colProdName, colProdCat;
    @FXML private TableColumn<Product, Double> colProdPrice;
    @FXML private TableColumn<Product, Integer> colProdStock;
    @FXML private TextField txtSku, txtName, txtPrice, txtStock;
    @FXML private ComboBox<String> cmbCategory;

    @FXML private TableView<Category> tblCategories;
    @FXML private TableColumn<Category, String> colCatId, colCatName;
    @FXML private TextField txtCategoryName;

    private ProductService productService;
    private CategoryService categoryService;

    @FXML
    public void initialize() {
        productService = new ProductService();
        categoryService = new CategoryService();

        colProdSku.setCellValueFactory(new PropertyValueFactory<>("sku"));
        colProdName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colProdCat.setCellValueFactory(new PropertyValueFactory<>("categoryId"));
        colProdPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colProdStock.setCellValueFactory(new PropertyValueFactory<>("stockQuantity"));

        colCatId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCatName.setCellValueFactory(new PropertyValueFactory<>("name"));

        // AUTO-FILL LOGIC: Click a row to put data into text fields
        tblProducts.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtSku.setText(newSelection.getSku());
                txtName.setText(newSelection.getName());
                txtPrice.setText(String.valueOf(newSelection.getPrice()));
                txtStock.setText(String.valueOf(newSelection.getStockQuantity()));
                cmbCategory.setValue(newSelection.getCategoryId());
            }
        });

        refreshData();
    }

    private void refreshData() {
        tblProducts.setItems(FXCollections.observableArrayList(productService.getAllProducts()));
        tblCategories.setItems(FXCollections.observableArrayList(categoryService.getAllCategories()));
        cmbCategory.getItems().clear();
        categoryService.getAllCategories().forEach(c -> cmbCategory.getItems().add(c.getName()));
    }

    @FXML
    public void addProduct(ActionEvent event) {
        Product p = new Product(txtSku.getText(), txtName.getText(), Double.parseDouble(txtPrice.getText()), cmbCategory.getValue(), Integer.parseInt(txtStock.getText()));
        productService.addProduct(p);
        refreshData();
    }

    @FXML
    public void editProduct(ActionEvent event) {
        Product selected = tblProducts.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selected.setName(txtName.getText());
            selected.setPrice(Double.parseDouble(txtPrice.getText()));
            selected.setCategoryId(cmbCategory.getValue());
            selected.setStockQuantity(Integer.parseInt(txtStock.getText()));
            productService.updateProduct(selected);
            refreshData();
        }
    }

    @FXML
    public void deleteProduct(ActionEvent event) {
        Product selected = tblProducts.getSelectionModel().getSelectedItem();
        if (selected != null) {
            productService.deleteProduct(selected.getSku());
            refreshData();
        }
    }

    @FXML
    public void addCategory(ActionEvent event) {
        Category c = new Category(txtCategoryName.getText().toLowerCase(), txtCategoryName.getText());
        categoryService.addCategory(c);
        refreshData();
        txtCategoryName.clear();
    }

    @FXML public void deleteCategory(ActionEvent event) {
        Category selected = tblCategories.getSelectionModel().getSelectedItem();
        if (selected != null) { categoryService.deleteCategory(selected.getId()); refreshData(); }
    }

    @FXML public void backToMain(ActionEvent event) { ((Stage) tblProducts.getScene().getWindow()).close(); }
}
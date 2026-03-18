package edu.mmcm.tradigital.model;

public class Product {
    private String sku;
    private String name;
    private double price;
    private String categoryId;
    private int stockQuantity;

    public Product() {
    }

    public Product(String sku, String name, double price, String categoryId, int stockQuantity) {
        this.sku = sku;
        this.name = name;
        this.price = price;
        this.categoryId = categoryId;
        this.stockQuantity = stockQuantity;
    }

    // --- Getters and Setters ---
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }

    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }
}
package edu.mmcm.tradigital.model;

public class OrderLine {
    private String productId;
    private int quantity;
    private double unitPrice;

    public OrderLine(String productId, int quantity, double unitPrice) {
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public String getProductId() { return productId; }
    public int getQuantity() { return quantity; }
    public double getUnitPrice() { return unitPrice; }
}
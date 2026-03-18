package edu.mmcm.tradigital.model;

import java.util.List;
import java.util.Date;
import java.util.UUID;

public class Order {
    private String orderId;
    private String userEmail;
    private List<Product> items;
    private double totalAmount;
    private Date orderDate;

    public Order(String userEmail, List<Product> items, double totalAmount) {
        // Automatically generates a random 8-character ID like "ORD-A1B2C3D4"
        this.orderId = "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.userEmail = userEmail;
        this.items = items;
        this.totalAmount = totalAmount;
        this.orderDate = new Date();
    }

    public String getOrderId() { return orderId; }
    public String getUserEmail() { return userEmail; }
    public List<Product> getItems() { return items; }
    public double getTotalAmount() { return totalAmount; }
    public Date getOrderDate() { return orderDate; }
}
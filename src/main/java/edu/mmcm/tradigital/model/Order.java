package edu.mmcm.tradigital.model;
import java.time.LocalDate;
import java.util.List;

public class Order {
    private String orderId;
    private String userId;
    private List<OrderLine> items;
    private String orderType;
    private double totalAmount;
    private LocalDate orderDate;
    private LocalDate dueDate;
    private String status;

    public Order(String orderId, String userId, List<OrderLine> items, String orderType, double totalAmount, LocalDate orderDate, LocalDate dueDate, String status) {
        this.orderId = orderId;
        this.userId = userId;
        this.items = items;
        this.orderType = orderType;
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
        this.dueDate = dueDate;
        this.status = status;
    }

    public String getOrderId() { return orderId; }
    public String getUserId() { return userId; }
    public List<OrderLine> getItems() { return items; }
    public String getOrderType() { return orderType; }
    public double getTotalAmount() { return totalAmount; }
    public LocalDate getOrderDate() { return orderDate; }
    public LocalDate getDueDate() { return dueDate; }
    public String getStatus() { return status; }
}
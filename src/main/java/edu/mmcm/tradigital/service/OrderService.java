package edu.mmcm.tradigital.service;

import edu.mmcm.tradigital.model.Order;
import edu.mmcm.tradigital.repo.OrderRepository;
import java.util.List;

public class OrderService {
    private final OrderRepository repository = new OrderRepository();

    public void addOrder(Order order) { repository.addOrder(order); }
    public List<Order> getAllOrders() { return repository.getAllOrders(); }
    public void updateOrderStatus(String orderId, String status) { repository.updateOrderStatus(orderId, status); }
    public void deleteOrder(String orderId) { repository.deleteOrder(orderId); }
}
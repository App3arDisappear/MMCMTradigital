package edu.mmcm.tradigital.service;

import edu.mmcm.tradigital.model.Order;
import edu.mmcm.tradigital.repo.OrderRepository;

public class OrderService {
    private final OrderRepository repository = new OrderRepository();

    public void saveOrder(Order order) {
        repository.saveOrder(order);
    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bricks.service;

import com.bricks.dom.Order;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

/**
 *
 * @author gouri
 */
@Service
public class OrdersService {
    
    private final Map<Long, Order> orders = new HashMap<>();
    private final AtomicLong counter = new AtomicLong();
    
    public Order createOrder(String name, Integer quantity) {
        Order order = new Order(counter.incrementAndGet(), name, quantity);
        orders.put(order.getId(), order);
        return order;
    }
    
    public Order getOrder(Long id) {
        return orders.get(id);
    }
    
    public Collection<Order> getAllOrders() {
        return orders.values();
    }
    
}

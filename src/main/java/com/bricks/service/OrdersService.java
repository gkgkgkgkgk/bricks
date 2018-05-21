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
    
    /**
     * To create an order
     * @param name
     * @param quantity
     * @return created order
     */
    public Order createOrder(String name, Integer quantity) {
        Order order = new Order(counter.incrementAndGet(), name, quantity);
        orders.put(order.getId(), order);
        return order;
    }
    
    /**
     * To get an order for the given id
     * @param id
     * @return order if found otherwise null
     */
    public Order getOrder(Long id) {
        return orders.get(id);
    }
    
    /**
     * To get all existing orders
     * @return collection
     */
    public Collection<Order> getAllOrders() {
        return orders.values();
    }
    
    /**
     * To update an existing order
     * @param order 
     */
    public void updateOrder(Order order) {
        orders.put(order.getId(), order);
    }
    
    /**
     * To set dispatch flag to true
     * @param id
     * @return 
     */
    public Order dispatchOrder(Long id) {
        Order order = orders.get(id);
        if(order != null) {
            order.setDispatched(true);
        }
        return order;
    }
}

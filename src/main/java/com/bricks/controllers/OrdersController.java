package com.bricks.controllers;

import com.bricks.dom.Order;
import com.bricks.service.OrdersService;
import java.util.Collection;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @GetMapping(value={"/", "/{id}"})
    public Collection<Order> get(@PathVariable(value="id", required = false) Long id) {
        Collection<Order> ordersToReturn = null;
        if (id == null) {
            ordersToReturn = ordersService.getAllOrders();
        } else {
            Order order = ordersService.getOrder(id);
            if (order != null) {
                ordersToReturn = Collections.singletonList(order);
            }
        } 
        return ordersToReturn;
    }
    
    @PostMapping
    public Order post(@RequestParam(value="name", required=true) String name, @RequestParam(value="quantity", required=true) Integer quantity) {
        return ordersService.createOrder(name, quantity);
    }
    
    
}

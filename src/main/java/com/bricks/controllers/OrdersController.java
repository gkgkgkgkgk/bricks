package com.bricks.controllers;

import com.bricks.dom.Order;
import com.bricks.exception.BadRequestException;
import com.bricks.service.OrdersService;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    
    @PutMapping
    public Order put(@RequestParam(value="id", required=true) Long id, @RequestParam(value="name", required=true) String name, @RequestParam(value="quantity", required=true) Integer quantity) {
        Order order = ordersService.getOrder(id);
        if (order != null) {
            order.setName(name);
            order.setQuantity(quantity);
            ordersService.updateOrder(order);
        } 
        return order;
    }
    
    @PatchMapping(value={"/{id}/dispatch"})
    public Order dispatch(@PathVariable(value="id", required=true) Long id) {
        Order order = ordersService.getOrder(id);
        if (order != null) {
            ordersService.dispatchOrder(order.getId());
        } else {
            throw new BadRequestException("Invalid order reference");
        }
        return order;
    }
    
    @ExceptionHandler(BadRequestException.class)
    void handleBadRequestException(BadRequestException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }
}

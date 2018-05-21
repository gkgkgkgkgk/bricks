/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bricks.service;

import com.bricks.dom.Order;
import java.util.Collection;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author gouri
 */
@RunWith(SpringRunner.class)
public class OrdersServiceTest {
    
    @Test
    public void testGetOrder() throws Exception {
        OrdersService ordersService = new OrdersService();
        
        //Given 
        Order order = ordersService.createOrder("test", 100);
        
        //When
        Order orderReturned = ordersService.getOrder(order.getId());
        
        //Then
        assertEquals(order.getId(), orderReturned.getId());
        assertEquals(order.getName(), orderReturned.getName());
        assertEquals(order.getQuantity().toString(), orderReturned.getQuantity().toString());
        
        //When
        orderReturned = ordersService.getOrder(1234l);
        
        //Then
        assertNull(orderReturned);
    }
    
    @Test
    public void testCreateOrder() throws Exception {
        OrdersService ordersService = new OrdersService();
        
        //When 
        Order order = ordersService.createOrder("test", 100);
        
        //Then
        assertNotNull(order.getId());
        assertEquals("test", order.getName());
        assertEquals("100", order.getQuantity().toString());
    }
    
    @Test
    public void testgetAllOrders() throws Exception {
        OrdersService ordersService = new OrdersService();
        
        //Given 
        ordersService.createOrder("test1", 100);
        ordersService.createOrder("test2", 400);
        ordersService.createOrder("test3", 800);
        
        //When
        Collection<Order>ordersReturned = ordersService.getAllOrders();
        
        //Then
        assertEquals(3, ordersReturned.size());
        assertThat(ordersReturned, hasItems(
                allOf(hasProperty("id", equalTo(1l)), hasProperty("name", is("test1")), hasProperty("quantity", is(100))),
                allOf(hasProperty("id", equalTo(2l)), hasProperty("name", is("test2")), hasProperty("quantity", is(400))),
                allOf(hasProperty("id", equalTo(3l)), hasProperty("name", is("test3")), hasProperty("quantity", is(800)))
                )
        );
    }

    @Test
    public void testUpdateOrder() throws Exception {
        OrdersService ordersService = new OrdersService();
        
        //Given 
        Order order = ordersService.createOrder("test", 100);
        
        //When
        order.setName("test1");
        order.setQuantity(200);
        ordersService.updateOrder(order);
        
        //Then
        Order orderReturned = ordersService.getOrder(order.getId());
        assertEquals(order.getId(), orderReturned.getId());
        assertEquals("test1", orderReturned.getName());
        assertEquals("200", orderReturned.getQuantity().toString());
    }
}

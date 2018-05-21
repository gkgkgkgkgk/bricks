package com.bricks.controllers;

import com.bricks.dom.Order;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class OrdersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCreateOrder() throws Exception {

        //When
        this.mockMvc.perform(post("/orders").param("name", "junit").param("quantity", "100"))
                //Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isNumber());
    }

    @Test
    public void testGetOrder() throws Exception {
        
        //Given customer has submitted an order for some bricks
        MvcResult result = this.mockMvc.perform(post("/orders").param("name", "junitget").param("quantity", "200"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isNumber())
                .andReturn();

        Order order = getOrder(result.getResponse().getContentAsString());
        
        //When "Get Order" request submitted with a valid Order reference
        result = this.mockMvc.perform(get("/orders/" + order.getId()))
                //Then order details are returned
                .andExpect(status().isOk())
                .andReturn();
        
        List<Order> ordersReturned = getOrders(result.getResponse().getContentAsString());
        
        assertEquals(1, ordersReturned.size());
        assertEquals(order.getId(), ordersReturned.get(0).getId());
        assertEquals(order.getName(), "junitget");
        assertEquals(order.getQuantity().toString(), "200");
        
        //When "Get Order" request submitted with an invalid Order reference
        this.mockMvc.perform(get("/orders/1234"))
                //Then order details are not returned
                .andExpect(status().isOk()).andExpect(content().string(""));;
        
        //Given many customer have submitted orders for some bricks
        this.mockMvc.perform(post("/orders").param("name", "junitget1").param("quantity", "400"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isNumber())
                .andReturn();
        this.mockMvc.perform(post("/orders").param("name", "junitget2").param("quantity", "600"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isNumber())
                .andReturn();
        
        //When "Get Order" request submitted with no Order reference
        result = this.mockMvc.perform(get("/orders/"))
                //Then all order details are returned
                .andExpect(status().isOk()).andReturn();
        
        ordersReturned = getOrders(result.getResponse().getContentAsString());
        
        assertEquals(3, ordersReturned.size());
        
        assertThat(ordersReturned, hasItems(
                allOf(hasProperty("id", equalTo(1l)), hasProperty("name", is("junitget")), hasProperty("quantity", is(200))),
                allOf(hasProperty("id", equalTo(2l)), hasProperty("name", is("junitget1")), hasProperty("quantity", is(400))),
                allOf(hasProperty("id", equalTo(3l)), hasProperty("name", is("junitget2")), hasProperty("quantity", is(600)))
                )
        );
    }
    
    private List<Order> getOrders(String content) throws IOException {
        return Arrays.asList(new ObjectMapper().readValue(content, Order[].class));
    }
    
    private Order getOrder(String content) throws IOException {
        return new ObjectMapper().readValue(content, Order.class);
    }
}
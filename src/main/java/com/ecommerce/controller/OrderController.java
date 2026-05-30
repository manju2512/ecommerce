package com.ecommerce.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.model.Order;
import com.ecommerce.repository.OrderRepository;

/**
 * ORDER CONTROLLER
 *
 * ENDPOINTS:
 *   GET  /api/orders              → all orders (admin)
 *   GET  /api/orders/user/{id}    → one customer's orders
 *   POST /api/orders              → place new order
 *   PUT  /api/orders/{id}/status  → update order status (admin)
 *
 * INTERVIEW: "The URL /api/orders/{id}/status uses a nested resource pattern.
 *             We're updating a specific property (status) of a specific order.
 *             @RequestBody String status receives the status as plain text or JSON string."
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository orderRepository;

    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // =========================================================================
    // GET ALL ORDERS — GET /api/orders  (Admin)
    // =========================================================================
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return ResponseEntity.ok(orders);
    }

    // =========================================================================
    // GET ORDERS BY USER — GET /api/orders/user/{userId}
    // Each customer can only see their own orders
    // =========================================================================
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUser(@PathVariable Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    // =========================================================================
    // PLACE ORDER — POST /api/orders
    // =========================================================================
    @PostMapping
    public ResponseEntity<Map<String, String>> placeOrder(@RequestBody Order order) {

        // Force status to Pending — frontend cannot set a different status
        order.setStatus("Pending");
        orderRepository.save(order);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Order placed successfully!");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // =========================================================================
    // UPDATE ORDER STATUS — PUT /api/orders/{id}/status  (Admin)
    // INTERVIEW: "@RequestBody String status — Spring reads the plain text
    //             from the request body as a String. The frontend sends
    //             "Shipped" or "Delivered" etc."
    // =========================================================================
    @PutMapping("/{id}/status")
    public ResponseEntity<Map<String, String>> updateStatus(
            @PathVariable Long id,
            @RequestBody String status) {

        // Clean any surrounding quotes from the JSON string body
        status = status.replace("\"", "").trim();
        orderRepository.updateStatus(id, status);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Order #" + id + " status updated to: " + status);
        return ResponseEntity.ok(response);
    }
}
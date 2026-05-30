package com.ecommerce.model;

import java.time.LocalDateTime;

/**
 * ORDER MODEL
 *
 * TABLE MAPPING (orders table):
 *   id          ← orders.id
 *   userId      ← orders.user_id     (Foreign Key → users.id)
 *   totalAmount ← orders.total_amount
 *   status      ← orders.status      ('Pending','Processing','Shipped','Delivered')
 *   orderDate   ← orders.order_date  (auto-set by MySQL on INSERT)
 *
 * INTERVIEW: "userId is a foreign key in the database. This means an order
 *             must belong to an existing user. MySQL enforces this constraint
 *             automatically — if you try to insert an order with a userId that
 *             doesn't exist in users table, MySQL throws an error."
 */
public class Order {

    private Long          id;
    private Long          userId;
    private double        totalAmount;
    private String        status;
    private LocalDateTime orderDate;

    // ---- Constructors ----

    public Order() {}

    public Order(Long id, Long userId, double totalAmount,
                 String status, LocalDateTime orderDate) {
        this.id          = id;
        this.userId      = userId;
        this.totalAmount = totalAmount;
        this.status      = status;
        this.orderDate   = orderDate;
    }

    // ---- Getters and Setters ----

    public Long getId()               { return id; }
    public void setId(Long id)        { this.id = id; }

    public Long getUserId()               { return userId; }
    public void setUserId(Long userId)    { this.userId = userId; }

    public double getTotalAmount()                  { return totalAmount; }
    public void setTotalAmount(double totalAmount)  { this.totalAmount = totalAmount; }

    public String getStatus()               { return status; }
    public void setStatus(String status)    { this.status = status; }

    public LocalDateTime getOrderDate()                   { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate)     { this.orderDate = orderDate; }
}
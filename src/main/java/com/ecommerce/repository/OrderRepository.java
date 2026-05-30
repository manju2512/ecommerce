package com.ecommerce.repository;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ecommerce.model.Order;

/**
 * ORDER REPOSITORY
 *
 * INTERVIEW: "The orders table has a foreign key constraint on user_id.
 *             This means MySQL automatically ensures every order has a
 *             valid user. This is called Referential Integrity."
 */
@Repository
public class OrderRepository {

    private final JdbcTemplate jdbc;

    public OrderRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    // -------------------------------------------------------------------------
    // ROW MAPPER
    // INTERVIEW: "rs.getTimestamp() returns a java.sql.Timestamp.
    //             .toLocalDateTime() converts it to the modern Java 8 type.
    //             This is important because ResultSet is old JDBC API (pre-Java 8)."
    // -------------------------------------------------------------------------
    private final RowMapper<Order> orderRowMapper = (rs, rowNum) -> {
        Order o = new Order();
        o.setId(rs.getLong("id"));
        o.setUserId(rs.getLong("user_id"));
        o.setTotalAmount(rs.getDouble("total_amount"));
        o.setStatus(rs.getString("status"));
        o.setOrderDate(rs.getTimestamp("order_date").toLocalDateTime());
        return o;
    };

    // -------------------------------------------------------------------------
    // GET ALL ORDERS (admin use)
    // Sorted newest first using ORDER BY order_date DESC
    // -------------------------------------------------------------------------
    public List<Order> findAll() {
        String sql = "SELECT * FROM orders ORDER BY order_date DESC";
        return jdbc.query(sql, orderRowMapper);
    }

    // -------------------------------------------------------------------------
    // GET ORDERS BY USER (customer's own orders)
    // -------------------------------------------------------------------------
    public List<Order> findByUserId(Long userId) {
        String sql = "SELECT * FROM orders WHERE user_id = ? ORDER BY order_date DESC";
        return jdbc.query(sql, orderRowMapper, userId);
    }

    // -------------------------------------------------------------------------
    // PLACE ORDER — insert new order
    // INTERVIEW: "order_date is NOT in the SQL because MySQL sets it automatically
    //             using DEFAULT CURRENT_TIMESTAMP defined in the schema."
    // -------------------------------------------------------------------------
    public void save(Order order) {
        String sql = "INSERT INTO orders (user_id, total_amount, status) VALUES (?, ?, ?)";
        jdbc.update(sql, order.getUserId(), order.getTotalAmount(), order.getStatus());
    }

    // -------------------------------------------------------------------------
    // UPDATE ORDER STATUS (admin use)
    // -------------------------------------------------------------------------
    public void updateStatus(Long id, String status) {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        jdbc.update(sql, status, id);
    }
}
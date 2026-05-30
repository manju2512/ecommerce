package com.ecommerce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ecommerce.model.User;

/**
 * USER REPOSITORY
 *
 * INTERVIEW EXPLANATION:
 * -----------------------
 * @Repository marks this class as a Data Access Object (DAO).
 * It tells Spring to:
 *   1. Create one instance of this class (a Bean) and manage it
 *   2. Translate JDBC SQLExceptions into Spring's DataAccessException hierarchy
 *
 * WHAT IS JdbcTemplate?
 *   JdbcTemplate is a Spring class that simplifies raw JDBC code.
 *
 *   RAW JDBC (without JdbcTemplate) needs you to:
 *     - Open a Connection
 *     - Create a PreparedStatement
 *     - Execute the query
 *     - Iterate ResultSet manually
 *     - Close everything in finally blocks
 *     = ~20 lines of boilerplate per query
 *
 *   JdbcTemplate (with Spring) handles ALL of that.
 *   You just write the SQL and a RowMapper.
 *   = ~3 lines per query
 *
 * WHAT IS A ROWMAPPER?
 *   A RowMapper tells JdbcTemplate HOW to convert one row from
 *   the ResultSet into a Java object (User, Product, etc.)
 *   rs = ResultSet (one row of database results)
 *   rowNum = which row number we're on (0, 1, 2...)
 *
 * WHAT IS Optional<T>?
 *   Optional avoids NullPointerException.
 *   Instead of returning null (which causes NPE), we return Optional.empty()
 *   The caller checks: if (result.isPresent()) { ... }
 */
@Repository
public class UserRepository {

    // JdbcTemplate is AUTO-INJECTED by Spring (Constructor Injection)
    // INTERVIEW: "Spring Boot auto-creates JdbcTemplate using the datasource
    //             configured in application.properties. We just declare it
    //             as a constructor parameter and Spring provides it."
    private final JdbcTemplate jdbc;

    public UserRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    // -------------------------------------------------------------------------
    // ROW MAPPER — converts one database row → User object
    // -------------------------------------------------------------------------
    private final RowMapper<User> userRowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setRole(rs.getString("role"));
        return user;
    };

    // -------------------------------------------------------------------------
    // FIND BY EMAIL — used during login
    // INTERVIEW: "? is a named parameter placeholder. JdbcTemplate replaces
    //             it with the actual value safely — this PREVENTS SQL Injection."
    // SQL Injection example WITHOUT prepared statements:
    //   email = "' OR '1'='1" → SELECT * FROM users WHERE email = '' OR '1'='1'
    //   This would return ALL users! PreparedStatement prevents this.
    // -------------------------------------------------------------------------
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        List<User> users = jdbc.query(sql, userRowMapper, email);
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    // -------------------------------------------------------------------------
    // SAVE — insert new user into database
    // jdbc.update() is used for INSERT, UPDATE, DELETE (any write operation)
    // -------------------------------------------------------------------------
    public void save(User user) {
        String sql = "INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, ?)";
        jdbc.update(sql, user.getName(), user.getEmail(), user.getPassword(), user.getRole());
    }

    // -------------------------------------------------------------------------
    // FIND ALL — returns every user (used by admin)
    // jdbc.query() is used for SELECT queries that return multiple rows
    // -------------------------------------------------------------------------
    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        return jdbc.query(sql, userRowMapper);
    }
}
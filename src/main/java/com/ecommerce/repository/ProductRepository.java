package com.ecommerce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ecommerce.model.Product;

/**
 * PRODUCT REPOSITORY
 *
 * INTERVIEW: "This class handles all CRUD operations for products.
 *   C = Create  → save()
 *   R = Read    → findAll(), findById()
 *   U = Update  → update()
 *   D = Delete  → delete()
 *
 * Each method has exactly one responsibility (Single Responsibility Principle).
 * We use JdbcTemplate to execute SQL and RowMapper to convert rows to objects."
 */
@Repository
public class ProductRepository {

    private final JdbcTemplate jdbc;

    public ProductRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    // -------------------------------------------------------------------------
    // ROW MAPPER
    // INTERVIEW: "image_url in the database maps to imageUrl in Java (camelCase).
    //             JDBC does NOT do this automatically — I do it manually here."
    // -------------------------------------------------------------------------
    private final RowMapper<Product> productRowMapper = (rs, rowNum) -> {
        Product p = new Product();
        p.setId(rs.getLong("id"));
        p.setName(rs.getString("name"));
        p.setDescription(rs.getString("description"));
        p.setPrice(rs.getDouble("price"));
        p.setStock(rs.getInt("stock"));
        p.setCategory(rs.getString("category"));
        p.setImageUrl(rs.getString("image_url"));
        return p;
    };

    // -------------------------------------------------------------------------
    // READ ALL PRODUCTS
    // -------------------------------------------------------------------------
    public List<Product> findAll() {
        String sql = "SELECT * FROM products ORDER BY id ASC";
        return jdbc.query(sql, productRowMapper);
    }

    // -------------------------------------------------------------------------
    // READ ONE PRODUCT BY ID
    // INTERVIEW: "queryForObject throws EmptyResultDataAccessException if not found.
    //             That's why I use query() and return Optional instead."
    // -------------------------------------------------------------------------
    public Optional<Product> findById(Long id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        List<Product> products = jdbc.query(sql, productRowMapper, id);
        return products.isEmpty() ? Optional.empty() : Optional.of(products.get(0));
    }

    // -------------------------------------------------------------------------
    // READ PRODUCTS BY CATEGORY
    // -------------------------------------------------------------------------
    public List<Product> findByCategory(String category) {
        String sql = "SELECT * FROM products WHERE category = ? ORDER BY id ASC";
        return jdbc.query(sql, productRowMapper, category);
    }

    // -------------------------------------------------------------------------
    // CREATE — insert new product
    // -------------------------------------------------------------------------
    public void save(Product p) {
        String sql = "INSERT INTO products (name, description, price, stock, category, image_url) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        jdbc.update(sql,
                p.getName(),
                p.getDescription(),
                p.getPrice(),
                p.getStock(),
                p.getCategory(),
                p.getImageUrl());
    }

    // -------------------------------------------------------------------------
    // UPDATE — modify existing product
    // -------------------------------------------------------------------------
    public void update(Product p) {
        String sql = "UPDATE products SET name=?, description=?, price=?, stock=?, " +
                     "category=?, image_url=? WHERE id=?";
        jdbc.update(sql,
                p.getName(),
                p.getDescription(),
                p.getPrice(),
                p.getStock(),
                p.getCategory(),
                p.getImageUrl(),
                p.getId());
    }

    // -------------------------------------------------------------------------
    // DELETE — remove product by id
    // -------------------------------------------------------------------------
    public void delete(Long id) {
        String sql = "DELETE FROM products WHERE id = ?";
        jdbc.update(sql, id);
    }
}
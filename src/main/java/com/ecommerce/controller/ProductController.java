package com.ecommerce.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.model.Product;
import com.ecommerce.repository.ProductRepository;

/**
 * PRODUCT CONTROLLER
 *
 * INTERVIEW EXPLANATION:
 * -----------------------
 * REST API Design — Each HTTP method has a specific meaning:
 *
 *   GET    → Read data    (safe, no side effects)
 *   POST   → Create data  (creates a new resource)
 *   PUT    → Update data  (replaces an existing resource)
 *   DELETE → Remove data  (deletes a resource)
 *
 * URL DESIGN (RESTful conventions):
 *   GET    /api/products       → get all products
 *   GET    /api/products/{id}  → get one product
 *   POST   /api/products       → add new product
 *   PUT    /api/products/{id}  → update product
 *   DELETE /api/products/{id}  → delete product
 *
 * @PathVariable vs @RequestBody vs @RequestParam:
 *   @PathVariable → extracts from URL: /products/{id} → id
 *   @RequestBody  → reads JSON from request body → Product object
 *   @RequestParam → reads from URL query string: /products?category=Electronics
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // =========================================================================
    // GET ALL PRODUCTS — GET /api/products
    // Anyone can view products (no authentication needed)
    // =========================================================================
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(
            @RequestParam(required = false) String category) {

        List<Product> products;

        if (category != null && !category.isEmpty()) {
            // Filter by category if ?category=Electronics is provided
            products = productRepository.findByCategory(category);
        } else {
            products = productRepository.findAll();
        }

        return ResponseEntity.ok(products);
    }

    // =========================================================================
    // GET ONE PRODUCT — GET /api/products/{id}
    // =========================================================================
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        Optional<Product> product = productRepository.findById(id);

        // INTERVIEW: "map() transforms the Optional<Product> to ResponseEntity.
        //             If present → 200 OK with product body.
        //             If empty   → 404 Not Found."
        return product
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // =========================================================================
    // ADD PRODUCT — POST /api/products  (Admin only)
    // =========================================================================
    @PostMapping
    public ResponseEntity<Map<String, String>> addProduct(@RequestBody Product product) {
        productRepository.save(product);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Product added successfully!");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // =========================================================================
    // UPDATE PRODUCT — PUT /api/products/{id}  (Admin only)
    // =========================================================================
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateProduct(
            @PathVariable Long id,
            @RequestBody Product product) {

        // Check if product exists before updating
        if (productRepository.findById(id).isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Product not found with id: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        product.setId(id);
        productRepository.update(product);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Product updated successfully!");
        return ResponseEntity.ok(response);
    }

    // =========================================================================
    // DELETE PRODUCT — DELETE /api/products/{id}  (Admin only)
    // =========================================================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable Long id) {

        if (productRepository.findById(id).isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Product not found with id: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        productRepository.delete(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Product deleted successfully!");
        return ResponseEntity.ok(response);
    }
}
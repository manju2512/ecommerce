package com.ecommerce.model;

/**
 * PRODUCT MODEL
 *
 * TABLE MAPPING (products table):
 *   id          ← products.id
 *   name        ← products.name
 *   description ← products.description
 *   price       ← products.price
 *   stock       ← products.stock
 *   category    ← products.category
 *   imageUrl    ← products.image_url   (note: Java uses camelCase, SQL uses snake_case)
 *
 * INTERVIEW: "In JDBC, I manually map snake_case column names to camelCase
 *             Java fields inside the RowMapper. JPA does this automatically
 *             but JDBC gives me explicit control."
 */
public class Product {

    private Long   id;
    private String name;
    private String description;
    private double price;
    private int    stock;
    private String category;
    private String imageUrl;

    // ---- Constructors ----

    public Product() {}

    public Product(Long id, String name, String description,
                   double price, int stock, String category, String imageUrl) {
        this.id          = id;
        this.name        = name;
        this.description = description;
        this.price       = price;
        this.stock       = stock;
        this.category    = category;
        this.imageUrl    = imageUrl;
    }

    // ---- Getters and Setters ----

    public Long getId()                   { return id; }
    public void setId(Long id)            { this.id = id; }

    public String getName()               { return name; }
    public void setName(String name)      { this.name = name; }

    public String getDescription()                    { return description; }
    public void setDescription(String description)    { this.description = description; }

    public double getPrice()              { return price; }
    public void setPrice(double price)    { this.price = price; }

    public int getStock()                 { return stock; }
    public void setStock(int stock)       { this.stock = stock; }

    public String getCategory()               { return category; }
    public void setCategory(String category)  { this.category = category; }

    public String getImageUrl()                 { return imageUrl; }
    public void setImageUrl(String imageUrl)    { this.imageUrl = imageUrl; }
}
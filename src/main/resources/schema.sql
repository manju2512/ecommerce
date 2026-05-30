-- =============================================
-- DATABASE AND TABLE CREATION
-- INTERVIEW POINT: "CREATE IF NOT EXISTS means this script is safe
-- to run multiple times — it won't fail if tables already exist."
-- =============================================

CREATE DATABASE IF NOT EXISTS ecommerce_db;
USE ecommerce_db;

-- ---------------------------------------------
-- USERS TABLE
-- Stores customer and admin accounts
-- role is either 'customer' or 'admin'
-- password is BCrypt hashed — never plain text
-- ---------------------------------------------
CREATE TABLE IF NOT EXISTS users (
    id       BIGINT       AUTO_INCREMENT PRIMARY KEY,
    name     VARCHAR(100) NOT NULL,
    email    VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(20)  NOT NULL DEFAULT 'customer'
);

-- ---------------------------------------------
-- PRODUCTS TABLE
-- Stores the product catalog
-- ---------------------------------------------
CREATE TABLE IF NOT EXISTS products (
    id          BIGINT         AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(200)   NOT NULL,
    description TEXT,
    price       DECIMAL(10, 2) NOT NULL,
    stock       INT            NOT NULL DEFAULT 0,
    category    VARCHAR(100),
    image_url   VARCHAR(500)
);

-- ---------------------------------------------
-- ORDERS TABLE
-- INTERVIEW POINT: "user_id is a Foreign Key. It references the users table.
-- This enforces referential integrity — you cannot place an order for
-- a user that doesn't exist."
-- status values: Pending → Processing → Shipped → Delivered
-- ---------------------------------------------
CREATE TABLE IF NOT EXISTS orders (
    id           BIGINT         AUTO_INCREMENT PRIMARY KEY,
    user_id      BIGINT         NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    status       VARCHAR(50)    NOT NULL DEFAULT 'Pending',
    order_date   TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

-- =============================================
-- SAMPLE DATA (INSERT IGNORE = skip if already exists)
-- =============================================

-- Admin user — password is "admin123" hashed with BCrypt
-- INTERVIEW: "I pre-hashed this password using BCryptPasswordEncoder
--             so the plain text never touches the database."
INSERT IGNORE INTO users (id, name, email, password, role)
VALUES (1, 'Admin User', 'admin@shop.com',
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'admin');

-- Sample products
INSERT IGNORE INTO products (id, name, description, price, stock, category, image_url) VALUES
(1, 'Laptop Pro',        'High-performance laptop, 16GB RAM, 512GB SSD',   999.99, 20, 'Electronics',
 'https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=400'),
(2, 'Wireless Headphones','Noise-cancelling over-ear wireless headphones',  149.99, 35, 'Electronics',
 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=400'),
(3, 'Cotton T-Shirt',    'Premium soft cotton casual t-shirt',               29.99, 100,'Clothing',
 'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=400'),
(4, 'Running Shoes',     'Lightweight breathable running shoes',             89.99, 50, 'Sports',
 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=400'),
(5, 'Coffee Maker',      'Automatic drip coffee maker, 12-cup capacity',     74.99, 25, 'Kitchen',
 'https://images.unsplash.com/photo-1495474472287-4d71bcdd2085?w=400'),
(6, 'Slim Fit Jeans',    'Classic slim fit denim jeans, multiple sizes',     49.99, 80, 'Clothing',
 'https://images.unsplash.com/photo-1542272604-787c3835535d?w=400'),
(7, 'Yoga Mat',          'Non-slip thick yoga and exercise mat',             34.99, 60, 'Sports',
 'https://images.unsplash.com/photo-1601925228040-f48e54fce3ad?w=400'),
(8, 'Smartwatch',        'Fitness tracking smartwatch with heart rate monitor',199.99,15,'Electronics',
 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=400');
# 🛒 E-Commerce Application

A full-stack e-commerce web application built with **Spring Boot**, **JDBC (JdbcTemplate)**, **MySQL**, and **Vanilla HTML/CSS/JS** — intentionally built without JPA or React to deeply understand raw SQL, JDBC, and DOM manipulation.

---

## 📌 Table of Contents

- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Features Implemented](#features-implemented)
- [Database Schema](#database-schema)
- [API Endpoints](#api-endpoints)
- [Getting Started](#getting-started)
- [Default Credentials](#default-credentials)
- [Known Limitations](#known-limitations)
- [Future Work (Production Roadmap)](#future-work-production-roadmap)

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Backend | Spring Boot 3.2.3, Java 17 |
| Data Access | Spring JDBC (JdbcTemplate) — no ORM |
| Database | MySQL 8 |
| Security | Spring Security (BCryptPasswordEncoder only) |
| Frontend | Vanilla HTML5, CSS3, JavaScript (ES6) |
| Build | Maven |
| Server | Embedded Apache Tomcat (port 8080) |

---

## Project Structure

```
ecommerce/
├── src/
│   ├── main/
│   │   ├── java/com/ecommerce/
│   │   │   ├── EcommerceApplication.java       # Entry point
│   │   │   ├── config/
│   │   │   │   └── SecurityConfig.java         # Spring Security config
│   │   │   ├── controller/
│   │   │   │   ├── UserController.java         # /api/register, /api/login
│   │   │   │   ├── ProductController.java      # /api/products CRUD
│   │   │   │   └── OrderController.java        # /api/orders CRUD
│   │   │   ├── model/
│   │   │   │   ├── User.java
│   │   │   │   ├── Product.java
│   │   │   │   └── Order.java
│   │   │   └── repository/
│   │   │       ├── UserRepository.java         # JDBC queries for users
│   │   │       ├── ProductRepository.java      # JDBC queries for products
│   │   │       └── OrderRepository.java        # JDBC queries for orders
│   │   └── resources/
│   │       ├── application.properties          # DB config, server port
│   │       ├── schema.sql                      # Auto-run on startup
│   │       └── static/                         # Frontend (served by Spring)
│   │           ├── index.html
│   │           ├── products.html
│   │           ├── cart.html
│   │           ├── orders.html
│   │           ├── login.html
│   │           ├── register.html
│   │           ├── admin.html
│   │           ├── css/style.css
│   │           └── js/
│   │               ├── config.js               # API_URL + auth/cart helpers
│   │               ├── auth.js                 # Login/register logic
│   │               ├── products.js             # Product listing + filter
│   │               ├── cart.js                 # Cart (localStorage)
│   │               ├── orders.js               # Order history
│   │               └── admin.js                # Admin panel
│   └── test/
│       └── java/com/ecommerce/
│           └── EcommerceApplicationTests.java
└── pom.xml
```

---

## Features Implemented

### Customer Features
- **Register** — create an account (email uniqueness enforced, BCrypt password hashing)
- **Login / Logout** — session stored in `localStorage`
- **Product Catalog** — browse all products with category filter
- **Product Detail** — view name, description, price, stock, image
- **Shopping Cart** — add/remove items, adjust quantity (persisted in `localStorage`)
- **Place Order** — checkout from cart, creates order in DB
- **Order History** — view all past orders with status and date

### Admin Features (role: `admin`)
- **Product Management** — add, edit, delete products via admin panel
- **Order Management** — view all orders, update order status (Pending → Processing → Shipped → Delivered)
- Admin panel access controlled client-side by role check

### Backend / API
- RESTful endpoints for users, products, and orders
- BCrypt password hashing on registration
- Duplicate email check on register
- Generic error message on login (prevents email enumeration)
- `schema.sql` auto-runs on startup to create tables and seed sample data

---

## Database Schema

```sql
-- Users
CREATE TABLE users (
    id       BIGINT       AUTO_INCREMENT PRIMARY KEY,
    name     VARCHAR(100) NOT NULL,
    email    VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,          -- BCrypt hashed
    role     VARCHAR(20)  NOT NULL DEFAULT 'customer'  -- 'customer' | 'admin'
);

-- Products
CREATE TABLE products (
    id          BIGINT         AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(200)   NOT NULL,
    description TEXT,
    price       DECIMAL(10,2)  NOT NULL,
    stock       INT            NOT NULL DEFAULT 0,
    category    VARCHAR(100),
    image_url   VARCHAR(500)
);

-- Orders
CREATE TABLE orders (
    id           BIGINT         AUTO_INCREMENT PRIMARY KEY,
    user_id      BIGINT         NOT NULL,
    total_amount DECIMAL(10,2)  NOT NULL,
    status       VARCHAR(50)    NOT NULL DEFAULT 'Pending',
    order_date   TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

Sample data (8 products + 1 admin user) is seeded automatically via `INSERT IGNORE` on startup.

---

## API Endpoints

### Auth — `/api`

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/register` | Register new customer | No |
| POST | `/api/login` | Login, returns user object | No |

### Products — `/api/products`

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/api/products` | Get all products | No |
| GET | `/api/products?category=Electronics` | Filter by category | No |
| GET | `/api/products/{id}` | Get one product | No |
| POST | `/api/products` | Add product | Admin only* |
| PUT | `/api/products/{id}` | Update product | Admin only* |
| DELETE | `/api/products/{id}` | Delete product | Admin only* |

### Orders — `/api/orders`

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/api/orders` | Get all orders | Admin only* |
| GET | `/api/orders/user/{userId}` | Get orders by user | Customer* |
| POST | `/api/orders` | Place new order | Customer* |
| PUT | `/api/orders/{id}/status` | Update order status | Admin only* |

> *Auth is currently enforced client-side only. See [Future Work](#future-work-production-roadmap).

---

## Getting Started

### Prerequisites
- Java 17+
- Maven 3.6+
- MySQL 8+

### 1. Clone the repository
```bash
git clone https://github.com/<your-username>/ecommerce.git
cd ecommerce
```

### 2. Configure the database
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

### 3. Run the application
```bash
mvn spring-boot:run
```

The app starts at `http://localhost:8080`. Tables and sample data are created automatically on first run.

### 4. Build a JAR
```bash
mvn clean package
java -jar target/ecommerce-1.0.0.jar
```

---

## Default Credentials

| Role | Email | Password |
|------|-------|----------|
| Admin | admin@shop.com | admin123 |

> Register any new account to get a `customer` role.

---

## Known Limitations

These are known gaps in the current implementation that are acceptable for a learning/demo project but must be addressed before production use:

- **No JWT / session tokens** — authentication state lives entirely in `localStorage`. The backend accepts all requests without verifying who is calling.
- **Admin role not enforced server-side** — any user can call `POST /api/products` or `DELETE /api/products/{id}` directly via curl or Postman.
- **Cart stored in localStorage only** — clearing browser data loses the cart. There is no `cart_items` table.
- **No order line items** — the `orders` table stores only `total_amount`. Individual products and quantities in an order are not persisted.
- **No stock decrement on order** — placing an order does not update `products.stock`. Overselling is possible.
- **Hardcoded DB password** — `application.properties` contains a plain-text password and should not be committed to a public repository. Replace with environment variables before pushing.
- **No input validation** — backend does not validate request body fields (e.g. negative price, null product name).
- **No pagination** — `GET /api/products` and `GET /api/orders` fetch all rows with no `LIMIT`, which will not scale.
- **No tests** — `EcommerceApplicationTests` contains no test methods.
- **`spring.sql.init.mode=always`** — schema.sql runs on every startup, which is unsafe against a live database.

---

## Future Work (Production Roadmap)

The following improvements are planned to make this platform production-ready, grouped by priority:

### Phase 1 — Security (Critical, before any deployment)
- [ ] Move all credentials to environment variables (`DB_PASSWORD`, etc.)
- [ ] Implement **JWT authentication** — issue token on login, validate on every API request via a filter
- [ ] Enforce **role-based access control** server-side (`ADMIN` role required for product/order management endpoints)
- [ ] Add **Bean Validation** (`@Valid`, `@NotBlank`, `@Positive`) on all request bodies + global `@ControllerAdvice` exception handler
- [ ] Configure explicit **CORS policy** for separated frontend/backend deployments
- [ ] Add **rate limiting** on `/api/login` to prevent brute-force attacks (Bucket4j)
- [ ] Enforce **password policy** on `/api/register` (minimum 8 characters)
- [ ] Enable **HTTPS** via reverse proxy (Nginx / cloud load balancer)

### Phase 2 — Data Integrity (before real users or payments)
- [ ] Add `order_items (order_id, product_id, quantity, unit_price)` table to persist line items
- [ ] Add `cart_items` table to persist cart server-side for logged-in users
- [ ] **Atomic stock decrement** on order placement — `UPDATE products SET stock = stock - ? WHERE id = ? AND stock >= ?` inside `@Transactional`
- [ ] **Razorpay signature verification** server-side (HMAC-SHA256) before saving order
- [ ] Introduce a **service layer** (`@Service`) to separate business logic from controllers and enable `@Transactional`
- [ ] Add **pagination** to product and order list endpoints (`LIMIT ? OFFSET ?`)
- [ ] Replace free-form status `String` with an `OrderStatus` enum

### Phase 3 — Code Quality & Testing
- [ ] Write **unit tests** with JUnit 5 + Mockito (register, login, placeOrder, updateStatus)
- [ ] Add **request/response DTOs** instead of exposing model classes directly in the API
- [ ] Switch to **Spring profiles** (`application-dev.properties` / `application-prod.properties`)
- [ ] Add **structured logging** via SLF4J; disable JDBC DEBUG logging in production
- [ ] Add **Springdoc OpenAPI** (Swagger UI at `/swagger-ui.html`) for API documentation

### Phase 4 — DevOps & Deployment
- [ ] Replace `spring.sql.init.mode=always` with **Flyway** versioned migrations
- [ ] Write a **Dockerfile** (multi-stage: Maven builder → JRE Alpine runtime)
- [ ] Add `docker-compose.yml` for local dev (app + MySQL containers)
- [ ] Configure **HikariCP** connection pool size for production load
- [ ] Add **Spring Boot Actuator** (`/actuator/health`) for container health checks
- [ ] Set up **GitHub Actions CI/CD** pipeline: test → build → deploy
- [ ] Replace hardcoded `API_URL` in `config.js` with a runtime environment variable

---

## Learning Goals

This project was built to understand:
- How `JdbcTemplate` simplifies raw JDBC (RowMapper, `query()` vs `update()`, prepared statements vs SQL injection)
- How BCrypt password hashing works (`encode()` vs `matches()`, random salt)
- REST API design (HTTP verbs, status codes, `@PathVariable` vs `@RequestBody` vs `@RequestParam`)
- Spring Security basics (`SecurityFilterChain`, `permitAll()`)
- Frontend auth flow using `localStorage` and role-based UI rendering
- How `schema.sql` auto-initialization works in Spring Boot

---

## Author

**Manjunatha S**  
B.E. Computer Science Engineering, RajaRajeswari College of Engineering, Bengaluru  
Internship: MindMatrix.io (Android Development)  
GitHub: [github.com/manju2512](https://github.com/manju2512)

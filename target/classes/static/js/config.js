// =============================================
// CENTRAL API CONFIGURATION
// Change this ONE line to switch between local and deployed server
// =============================================
const API_URL = "http://localhost:8080";

// =============================================
// AUTH HELPERS — used across all pages
// =============================================

// Save logged-in user to browser localStorage
function saveUser(user) {
    localStorage.setItem("ecomUser", JSON.stringify(user));
}

// Get the logged-in user object
function getUser() {
    const data = localStorage.getItem("ecomUser");
    return data ? JSON.parse(data) : null;
}

// Clear user session (logout)
function clearUser() {
    localStorage.removeItem("ecomUser");
}

// Redirect to login if not logged in
function requireLogin() {
    if (!getUser()) {
        window.location.href = "/login.html";
    }
}

// Redirect to home if not admin
function requireAdmin() {
    const user = getUser();
    if (!user || user.role !== "admin") {
        window.location.href = "/index.html";
    }
}

// =============================================
// CART HELPERS — cart stored in localStorage
// =============================================

function getCart() {
    const data = localStorage.getItem("ecomCart");
    return data ? JSON.parse(data) : [];
}

function saveCart(cart) {
    localStorage.setItem("ecomCart", JSON.stringify(cart));
}

function getCartCount() {
    return getCart().reduce((sum, item) => sum + item.quantity, 0);
}

// =============================================
// NAVBAR — injected into every page
// =============================================
function renderNavbar() {
    const user = getUser();
    const cartCount = getCartCount();

    const navbar = document.getElementById("navbar");
    if (!navbar) return;

    navbar.innerHTML = `
        <div class="nav-container">
            <a href="/index.html" class="nav-logo">🛒 E-Shop</a>
            <div class="nav-links">
                <a href="/index.html">Home</a>
                <a href="/products.html">Products</a>
                <a href="/cart.html">Cart 
                    <span class="cart-badge" id="cartBadge">${cartCount > 0 ? cartCount : ""}</span>
                </a>
                ${user ? `
                    <a href="/orders.html">My Orders</a>
                    ${user.role === "admin" ? `<a href="/admin.html">⚙ Admin</a>` : ""}
                    <span class="nav-user">Hi, ${user.name}</span>
                    <button onclick="logout()" class="btn-logout">Logout</button>
                ` : `
                    <a href="/login.html">Login</a>
                    <a href="/register.html" class="btn-nav-register">Register</a>
                `}
            </div>
        </div>
    `;
}

function logout() {
    clearUser();
    window.location.href = "/login.html";
}

function updateCartBadge() {
    const badge = document.getElementById("cartBadge");
    const count = getCartCount();
    if (badge) badge.textContent = count > 0 ? count : "";
}
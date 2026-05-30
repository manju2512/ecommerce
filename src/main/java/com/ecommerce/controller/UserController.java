package com.ecommerce.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.model.User;
import com.ecommerce.repository.UserRepository;

/**
 * USER CONTROLLER
 *
 * INTERVIEW EXPLANATION:
 * -----------------------
 * @RestController = @Controller + @ResponseBody
 *   - @Controller  → This class handles HTTP requests
 *   - @ResponseBody → Return values are written as JSON directly to the response
 *                     (not forwarded to a JSP/HTML view)
 *
 * @RequestMapping("/api") → All endpoints in this class start with /api
 *
 * ENDPOINTS:
 *   POST /api/register → Create a new user account
 *   POST /api/login    → Authenticate user, return user data
 *
 * WHAT IS ResponseEntity?
 *   ResponseEntity wraps the response body AND lets you set the HTTP status code.
 *   Examples:
 *     ResponseEntity.ok(data)                      → 200 OK
 *     ResponseEntity.status(HttpStatus.CREATED)    → 201 Created
 *     ResponseEntity.status(HttpStatus.BAD_REQUEST)→ 400 Bad Request
 *     ResponseEntity.status(HttpStatus.UNAUTHORIZED)→ 401 Unauthorized
 *
 * INTERVIEW: "HTTP status codes communicate the result to the frontend.
 *             200 = success, 201 = resource created, 400 = bad request,
 *             401 = unauthorized, 404 = not found, 500 = server error."
 *
 * WHAT IS @RequestBody?
 *   Jackson reads the JSON from the HTTP request body and converts it
 *   automatically into a Java object (User, Map, etc.)
 *   Example: {"email":"a@b.com","password":"123"} → Map<String,String>
 */
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserRepository      userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // Constructor Injection — Spring provides both beans automatically
    public UserController(UserRepository userRepository,
                          BCryptPasswordEncoder passwordEncoder) {
        this.userRepository  = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // =========================================================================
    // REGISTER — POST /api/register
    // =========================================================================
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody User user) {

        Map<String, String> response = new HashMap<>();

        // Step 1: Check if email already exists
        // INTERVIEW: "We check for duplicate emails BEFORE inserting,
        //             because the database also has a UNIQUE constraint on email.
        //             We catch it early to give a friendly error message."
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            response.put("message", "Email is already registered.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Step 2: Hash the password — NEVER save plain text
        // INTERVIEW: "BCrypt generates a random salt and embeds it in the hash.
        //             Two calls to encode() with the same password produce
        //             DIFFERENT hashes — but matches() still returns true."
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Step 3: Force role to 'customer' — users cannot self-assign 'admin'
        user.setRole("customer");

        // Step 4: Save to database
        userRepository.save(user);

        response.put("message", "Registration successful! Please login.");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // =========================================================================
    // LOGIN — POST /api/login
    // =========================================================================
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> request) {

        Map<String, Object> response = new HashMap<>();
        String email    = request.get("email");
        String password = request.get("password");

        // Step 1: Find user by email
        Optional<User> userOptional = userRepository.findByEmail(email);

        // Step 2: Verify password using BCrypt matches()
        // INTERVIEW: "We use Optional to safely handle the case where the email
        //             doesn't exist, without NullPointerException."
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (passwordEncoder.matches(password, user.getPassword())) {

                // Step 3: Build a SAFE user object (exclude the password!)
                // INTERVIEW: "We NEVER send the hashed password back to the frontend.
                //             That's a security risk. We send only what the UI needs."
                Map<String, Object> safeUser = new HashMap<>();
                safeUser.put("id",    user.getId());
                safeUser.put("name",  user.getName());
                safeUser.put("email", user.getEmail());
                safeUser.put("role",  user.getRole());

                response.put("message", "Login successful!");
                response.put("user",    safeUser);
                return ResponseEntity.ok(response);
            }
        }

        // Step 4: Generic error — don't tell attacker if email exists or password wrong
        // INTERVIEW: "Security best practice: give the same error for both
        //             'email not found' and 'wrong password'. This prevents
        //             attackers from discovering valid email addresses."
        response.put("message", "Invalid email or password.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}
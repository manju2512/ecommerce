package com.ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * SECURITY CONFIGURATION
 *
 * INTERVIEW EXPLANATION:
 * -----------------------
 * Spring Security, when added to classpath, blocks ALL requests by default
 * and shows a login page. We don't want that — we have our own HTML login page.
 *
 * So this class does two things:
 *   1. Disables Spring Security's default blocking behaviour
 *      → Allows all HTTP requests to reach our controllers freely
 *
 *   2. Provides BCryptPasswordEncoder as a Spring Bean
 *      → Used in UserController to hash passwords before saving to DB
 *
 * WHY BCRYPT?
 *   BCrypt is a one-way hashing algorithm. You cannot reverse it.
 *   Even if the database is stolen, attackers cannot get the real passwords.
 *
 *   Example:
 *     Plain text: "mypassword123"
 *     BCrypt:     "$2a$10$abc...xyz" (60 character hash)
 *
 *   passwordEncoder.encode("mypassword123") → produces the hash
 *   passwordEncoder.matches("mypassword123", storedHash) → returns true/false
 *
 * WHY @Bean?
 *   @Bean tells Spring: "Create this object and manage it for me."
 *   Any other class can then inject it using constructor injection.
 *   This is the Inversion of Control (IoC) principle.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Disable Spring Security's default login page and allow all requests.
     * INTERVIEW: "csrf().disable() turns off CSRF protection because our
     *             frontend uses plain fetch() with JSON — not HTML form POST.
     *             In production, you would implement proper CSRF tokens or JWT."
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            );
        return http.build();
    }

    /**
     * BCryptPasswordEncoder Bean
     * INTERVIEW: "By declaring this as @Bean, Spring manages one single instance.
     *             UserController gets it via constructor injection — this is
     *             Dependency Injection, a core Spring concept."
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
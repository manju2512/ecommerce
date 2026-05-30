package com.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ENTRY POINT OF THE APPLICATION
 *
 * INTERVIEW EXPLANATION:
 * -----------------------
 * @SpringBootApplication is a shortcut for 3 annotations:
 *
 *   1. @Configuration      → This class can define Spring Beans
 *   2. @EnableAutoConfiguration → Spring Boot auto-configures things like
 *                                  DataSource, JdbcTemplate, Tomcat server
 *                                  based on what JARs are on the classpath
 *   3. @ComponentScan      → Scans this package and sub-packages for
 *                            @Controller, @Service, @Repository, @Component
 *
 * HOW IT WORKS:
 *   SpringApplication.run() starts the embedded Tomcat server and
 *   loads the entire Spring context (all beans, configurations, etc.)
 *
 * WHY EMBEDDED TOMCAT?
 *   Traditional Java apps needed an external Tomcat server.
 *   Spring Boot bundles Tomcat inside the JAR — so you just run:
 *   java -jar ecommerce.jar
 *   No separate server installation needed.
 */
@SpringBootApplication
public class EcommerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommerceApplication.class, args);
        System.out.println("========================================");
        System.out.println("  E-Commerce App is running!");
        System.out.println("  Visit: http://localhost:8080");
        System.out.println("========================================");
    }
}
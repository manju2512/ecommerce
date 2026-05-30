package com.ecommerce.model;

/**
 * USER MODEL (Plain Old Java Object — POJO)
 *
 * INTERVIEW EXPLANATION:
 * -----------------------
 * A Model class represents a real-world entity in code.
 * Each field maps directly to a column in the 'users' database table.
 *
 * WHY NO @Entity ANNOTATION?
 *   We are using JDBC (not JPA/Hibernate). In JPA you annotate the class
 *   with @Entity and Spring generates the SQL. With JDBC, WE write the SQL
 *   manually in the Repository — which gives us full control.
 *
 * TABLE MAPPING:
 *   id       ← users.id
 *   name     ← users.name
 *   email    ← users.email
 *   password ← users.password  (BCrypt hashed, never plain text)
 *   role     ← users.role      ('customer' or 'admin')
 */
public class User {

    private Long   id;
    private String name;
    private String email;
    private String password;
    private String role;

    // ---- Constructors ----

    public User() {}

    public User(Long id, String name, String email, String password, String role) {
        this.id       = id;
        this.name     = name;
        this.email    = email;
        this.password = password;
        this.role     = role;
    }

    // ---- Getters and Setters ----
    // INTERVIEW: "Getters/setters allow controlled access to private fields.
    //             Jackson (JSON library) also uses these to convert User to JSON."

    public Long getId()                 { return id; }
    public void setId(Long id)          { this.id = id; }

    public String getName()             { return name; }
    public void setName(String name)    { this.name = name; }

    public String getEmail()              { return email; }
    public void setEmail(String email)    { this.email = email; }

    public String getPassword()               { return password; }
    public void setPassword(String password)  { this.password = password; }

    public String getRole()             { return role; }
    public void setRole(String role)    { this.role = role; }
}
package com.pms4st.pms.entity;

import jakarta.persistence.*; // For annotations like @Entity, @Id, etc.
import lombok.Data; // Lombok: Less code for getters/setters/etc.
import lombok.NoArgsConstructor; // Lombok: Creates empty constructor

import java.util.HashSet; // Used for collections of related items
import java.util.Set;

@Entity // Tells JPA this class represents a table
@Table(name = "users") // Links to the "users" table in the DB
@Data // Lombok adds getters, setters, toString, etc. automatically
@NoArgsConstructor // Lombok adds a constructor with no arguments
public class User {

    @Id // Marks this field as the Primary Key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tells DB to auto-generate the ID
    private Long id;

    @Column(nullable = false, unique = true) // Maps to a column, cannot be null, must be unique
    private String username;

    @Column(nullable = false) // Password column, cannot be null
    private String password; // IMPORTANT: We will store a HASHED password here, not plain text!

    @Column(nullable = false, unique = true)
    private String email;

    private String fullName; // Maps to full_name column, nullable by default

    @Column(nullable = false)
    private boolean enabled = true; // For Spring Security login status

    // Relationship: A User can have many Roles (e.g., ROLE_USER, ROLE_ADMIN)
    // FetchType.EAGER means load the roles immediately when loading a User.
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable( // Defines the link table used for Many-to-Many
            name = "user_roles", // Name of the link table in the DB
            joinColumns = @JoinColumn(name = "user_id"), // Foreign key in link table pointing back to User
            inverseJoinColumns = @JoinColumn(name = "role_id") // Foreign key in link table pointing to Role
    )
    private Set<Role> roles = new HashSet<>(); // A user can have a set of roles
}
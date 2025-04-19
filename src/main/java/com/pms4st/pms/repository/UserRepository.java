package com.pms4st.pms.repository;

import com.pms4st.pms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository; // Good practice to add @Repository

import java.util.Optional; // Represents a value that might be null

@Repository // Tells Spring this is a Data Access Component
// JpaRepository<EntityType, TypeOfPrimaryKey> gives us save(), findById(), findAll(), deleteById(), etc.
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring Data JPA automatically creates the query for this method name:
    // Finds one User by its username. Optional handles if user is not found.
    Optional<User> findByUsername(String username);

    // Finds one User by its email.
    Optional<User> findByEmail(String email);

    // Checks if a user exists with that username (returns true/false).
    boolean existsByUsername(String username);

    // Checks if a user exists with that email (returns true/false).
    boolean existsByEmail(String email);
}
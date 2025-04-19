package com.pms4st.pms.repository;

import com.pms4st.pms.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    // Find a role by its exact name (e.g., "ROLE_USER")
    Optional<Role> findByName(String name);
}
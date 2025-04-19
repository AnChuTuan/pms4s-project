package com.pms4st.pms.repository;

import com.pms4st.pms.entity.Project;
import com.pms4st.pms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // For custom queries
import org.springframework.data.repository.query.Param; // For naming parameters in queries
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    // Find projects owned by a specific user
    List<Project> findByOwner(User owner);

    // Find projects where a specific user is listed in the 'members' collection
    // This uses JPQL (Java Persistence Query Language) - similar to SQL but uses entity/field names
    @Query("SELECT p FROM Project p JOIN p.members m WHERE m = :user")
    List<Project> findProjectsWhereUserIsMember(@Param("user") User user);

}
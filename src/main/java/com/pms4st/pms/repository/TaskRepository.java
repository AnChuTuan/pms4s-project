package com.pms4st.pms.repository;

import com.pms4st.pms.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // Find all tasks belonging to a specific project ID
    List<Task> findByProjectId(Long projectId);

    // Find all tasks assigned to a specific user ID (assignee)
    List<Task> findByAssigneeId(Long userId);
}
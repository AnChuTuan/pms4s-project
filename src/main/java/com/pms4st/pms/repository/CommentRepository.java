package com.pms4st.pms.repository;

import com.pms4st.pms.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional; // For delete method

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // Find comments directly on a project (task ID is null), newest first
    List<Comment> findByProjectIdAndTaskIdIsNullOrderByCreatedAtDesc(Long projectId);

    // Find comments on a specific task, newest first
    List<Comment> findByTaskIdOrderByCreatedAtDesc(Long taskId);

    // Needed by ProjectService to manually delete project-level comments
    // Modifying data requires @Transactional
    @Transactional
    void deleteByProjectIdAndTaskIdIsNull(Long projectId);
}
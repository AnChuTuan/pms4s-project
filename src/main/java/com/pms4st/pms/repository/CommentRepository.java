package com.pms4st.pms.repository;
import com.pms4st.pms.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByProjectIdAndTaskIsNullOrderByCreatedAtDesc(Long projectId);
    List<Comment> findByTaskIdOrderByCreatedAtDesc(Long taskId);
    @Transactional void deleteByProjectIdAndTaskIsNull(Long projectId);
}
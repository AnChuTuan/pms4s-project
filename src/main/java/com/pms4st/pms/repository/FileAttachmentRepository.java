package com.pms4st.pms.repository;

import com.pms4st.pms.entity.FileAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileAttachmentRepository extends JpaRepository<FileAttachment, Long> {

    // Find attachments directly on a project, newest first
    List<FileAttachment> findByProjectIdAndTaskIdIsNullOrderByUploadTimeDesc(Long projectId);

    // Find attachments on a specific task, newest first
    List<FileAttachment> findByTaskIdOrderByUploadTimeDesc(Long taskId);

    // Find attachment by its unique stored filename (for download/delete)
    Optional<FileAttachment> findByStoredFileName(String storedFileName);

    // Find project-level attachments (needed for manual deletion in ProjectService)
    List<FileAttachment> findByProjectIdAndTaskIdIsNull(Long projectId);
}
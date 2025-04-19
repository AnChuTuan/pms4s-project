package com.pms4st.pms.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "file_attachments")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"uploader", "project", "task"})
@ToString(exclude = {"uploader", "project", "task"})
public class FileAttachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName; // Original filename

    @Column(nullable = false, unique = true)
    private String storedFileName; // Unique name for storage

    @Column(length = 100)
    private String contentType; // e.g., "image/png"

    private Long size; // Size in bytes

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime uploadTime;

    // Relationship: Many files uploaded by one User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploader_id") // Nullable based on DB ON DELETE SET NULL
    private User uploader;

    // Relationship: File linked to a Project (or null)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id") // Nullable
    private Project project;

    // Relationship: File linked to a Task (or null)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id") // Nullable
    private Task task;
}
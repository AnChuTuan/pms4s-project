package com.pms4st.pms.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp; // Automatic timestamp

import java.time.LocalDateTime; // Use for date and time

@Entity
@Table(name = "comments")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"user", "project", "task"})
@ToString(exclude = {"user", "project", "task"})
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @CreationTimestamp // Automatically set when created
    @Column(nullable = false, updatable = false) // Cannot be null or updated later
    private LocalDateTime createdAt;

    // Relationship: Many comments by one User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // Nullable based on DB ON DELETE SET NULL
    private User user;

    // Relationship: Comment linked to a Project (or null)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id") // Nullable
    private Project project;

    // Relationship: Comment linked to a Task (or null)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id") // Nullable
    private Task task;
}
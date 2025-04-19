package com.pms4st.pms.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"project", "assignee", "createdBy"}) // Avoid loops
@ToString(exclude = {"project", "assignee", "createdBy"}) // Avoid loops
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relationship: Many tasks belong to one Project
    @ManyToOne(fetch = FetchType.LAZY, optional = false) // Task *must* have a project
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(nullable = false, length = 200)
    private String name;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, length = 20)
    private String status = "TODO"; // Default value

    @Column(nullable = false, length = 20)
    private String priority = "MEDIUM"; // Default value

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate deadline;

    // Relationship: Many tasks can be assigned to one User (or null)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_id") // Nullable is default for ManyToOne
    private User assignee;

    // Relationship: Many tasks created by one User
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by_id", nullable = false)
    private User createdBy;

    // Relationship: One task can have many comments
    // Cascade REMOVE: If task deleted, delete its comments
    @OneToMany(mappedBy = "task", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Comment> comments = new HashSet<>();

    // Relationship: One task can have many attachments
    @OneToMany(mappedBy = "task", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<FileAttachment> attachments = new HashSet<>();
}
package com.pms4st.pms.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode; // Helps Lombok avoid errors with relationships
import lombok.NoArgsConstructor;
import lombok.ToString; // Helps Lombok avoid errors with relationships
import org.springframework.format.annotation.DateTimeFormat; // Helps with date input from forms

import java.time.LocalDate; // Use modern Java Date/Time API
import java.util.HashSet;
import java.util.Set;
import com.pms4st.pms.entity.FileAttachment;
import com.pms4st.pms.entity.Comment;
import com.pms4st.pms.entity.Task;

@Entity
@Table(name = "projects")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"owner", "tasks", "members"}) // Avoid infinite loops in generated methods
@ToString(exclude = {"owner", "tasks", "members"}) // Avoid infinite loops in generated methods
public class Project {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Lob // Suggests a large text field (like TEXT in SQL)
    @Column(columnDefinition = "TEXT") // Explicitly define column type if needed
    private String description;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) // Format YYYY-MM-DD for forms
    private LocalDate startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;

    // Relationship: Many projects can belong to one User (the owner)
    // FetchType.LAZY means load the owner only when needed (better performance)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false) // Links to the owner_id foreign key column
    private User owner;

    // Relationship: One project can have many Tasks
    // mappedBy="project": Links to the 'project' field in the Task class
    // cascade=CascadeType.ALL: If project saved/deleted, also save/delete its tasks
    // orphanRemoval=true: If a task is removed from this project's set, delete the task record
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Task> tasks = new HashSet<>();

    // Relationship: Many Projects can have many Users as members
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "project_memberships", // The link table
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> members = new HashSet<>();

    // Relationships for comments/attachments directly on project
    // Note: Deletion needs manual handling in Service due to DB constraints we set
    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<FileAttachment> attachments = new HashSet<>();
}
package com.pms4st.pms.entity;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity @Table(name = "comments")
public class Comment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Lob @Column(nullable = false, columnDefinition = "TEXT") private String content;
    @CreationTimestamp @Column(nullable = false, updatable = false) private LocalDateTime createdAt;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id") private User user;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "project_id") private Project project;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "task_id") private Task task;
    // --- Basic Getters/Setters/Constructors ---
    public Comment() {}
    public Long getId() {return id;} public void setId(Long id) {this.id = id;}
    public String getContent() {return content;} public void setContent(String c) {this.content = c;}
    public LocalDateTime getCreatedAt() {return createdAt;} public void setCreatedAt(LocalDateTime c) {this.createdAt = c;}
    public User getUser() {return user;} public void setUser(User u) {this.user = u;}
    public Project getProject() {return project;} public void setProject(Project p) {this.project = p;}
    public Task getTask() {return task;} public void setTask(Task t) {this.task = t;}
    @Override public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof Comment c)) return false; return id != null && id.equals(c.id); }
    @Override public int hashCode() { return Objects.hash(id); }
}
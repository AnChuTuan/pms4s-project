package com.pms4st.pms.entity;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity @Table(name = "tasks")
public class Task {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "project_id", nullable = false) private Project project;
    @Column(nullable = false, length = 200) private String name;
    @Lob @Column(columnDefinition = "TEXT") private String description;
    @Column(nullable = false, length = 20) private String status = "TODO";
    @Column(nullable = false, length = 20) private String priority = "MEDIUM";
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) private LocalDate deadline;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "assignee_id") private User assignee;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "created_by_id", nullable = false) private User createdBy;
    @OneToMany(mappedBy = "task", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY) private Set<Comment> comments = new HashSet<>();
    // --- Basic Getters/Setters/Constructors ---
    public Task() {}
    public Long getId() {return id;} public void setId(Long id) {this.id = id;}
    public Project getProject() {return project;} public void setProject(Project p) {this.project = p;}
    public String getName() {return name;} public void setName(String n) {this.name = n;}
    public String getDescription() {return description;} public void setDescription(String d) {this.description = d;}
    public String getStatus() {return status;} public void setStatus(String s) {this.status = s;}
    public String getPriority() {return priority;} public void setPriority(String p) {this.priority = p;}
    public LocalDate getDeadline() {return deadline;} public void setDeadline(LocalDate d) {this.deadline = d;}
    public User getAssignee() {return assignee;} public void setAssignee(User a) {this.assignee = a;}
    public User getCreatedBy() {return createdBy;} public void setCreatedBy(User cb) {this.createdBy = cb;}
    public Set<Comment> getComments() { return comments; } public void setComments(Set<Comment> c) { this.comments = c; }
    @Override public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof Task t)) return false; return id != null && id.equals(t.id); }
    @Override public int hashCode() { return Objects.hash(id); }
}
package com.pms4st.pms.entity; // Use your actual base package

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "projects")
public class Project {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false) private String name;
    @Lob @Column(columnDefinition = "TEXT") private String description;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) private LocalDate startDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) private LocalDate endDate;

    // Many Projects to One Owner (User)
    @ManyToOne(fetch = FetchType.LAZY) // Keep owner LAZY unless needed everywhere
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    // One Project to Many Tasks
    // Keep tasks LAZY unless you always need them with the project
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Task> tasks = new HashSet<>();

    // Many Projects have Many Members (Users)
    // *** MODIFIED HERE: Changed LAZY to EAGER ***
    @ManyToMany(fetch = FetchType.EAGER) // Load members immediately with the project
    @JoinTable(
            name = "project_memberships",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> members = new HashSet<>();

    // Keep comments LAZY unless always needed
    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Comment> comments = new HashSet<>();

    // File Attachments relationship removed as requested previously

    // --- Basic Getters/Setters/Constructors ---
    public Project() {}
    public Long getId() {return id;} public void setId(Long id) {this.id = id;}
    public String getName() {return name;} public void setName(String n) {this.name = n;}
    public String getDescription() {return description;} public void setDescription(String d) {this.description = d;}
    public LocalDate getStartDate() {return startDate;} public void setStartDate(LocalDate sd) {this.startDate = sd;}
    public LocalDate getEndDate() {return endDate;} public void setEndDate(LocalDate ed) {this.endDate = ed;}
    public User getOwner() {return owner;} public void setOwner(User o) {this.owner = o;}
    public Set<Task> getTasks() {return tasks;} public void setTasks(Set<Task> t) {this.tasks = t;}
    public Set<User> getMembers() {return members;} public void setMembers(Set<User> m) {this.members = m;}
    public Set<Comment> getComments() { return comments; } public void setComments(Set<Comment> c) { this.comments = c; }
    // Note: Removed getters/setters for 'attachments' as the field was removed

    @Override public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof Project p)) return false; return id != null && id.equals(p.id); }
    @Override public int hashCode() { return Objects.hash(id); }

    @Override public String toString() { return "Project{id=" + id + ", name='" + name + "'}"; } // Basic toString
}
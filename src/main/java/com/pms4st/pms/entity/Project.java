package com.pms4st.pms.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Project {

    private Long id;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private User owner;
    private Set<User> members = new HashSet<>();

    public Project() {}

    public Project(String name, String description, LocalDate startDate, LocalDate endDate, User owner) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.owner = owner;
    }

    // Getters & Setters
    public Long getId() { return id;}
    public void setId(Long id) { this.id = id;}

    public String getName() { return name;}
    public void setName(String name) { this.name = name;}

    public String getDescription() { return description;}
    public void setDescription(String description) { this.description = description;}

    public LocalDate getStartDate() { return startDate;}
    public void setStartDate(LocalDate startDate) { this.startDate = startDate;}

    public LocalDate getEndDate() { return endDate;}
    public void setEndDate(LocalDate endDate) { this.endDate = endDate;}

    public User getOwner() { return owner;}
    public void setOwner(User owner) { this.owner = owner;}

    public Set<User> getMembers() { return members;}
    public void setMembers(Set<User> members) { this.members = members;}

    public void addMember(User user) {
        this.members.add(user);
    }

    public void removeMember(User user) {
        this.members.remove(user);
    }

    public String toString() {
        return "Project{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", owner=" + (owner != null ? owner.getUsername() : null) +
            '}';
    }
}

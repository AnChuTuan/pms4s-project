package com.pms4st.pms.entity;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity @Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, unique = true) private String username;
    @Column(nullable = false) private String password;
    @Column(nullable = false, unique = true) private String email;
    private String fullName;
    @Column(nullable = false) private boolean enabled = true;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    // --- Basic Getters/Setters/Constructors ---
    public User() {}
    public Long getId() {return id;} public void setId(Long id) {this.id = id;}
    public String getUsername() {return username;} public void setUsername(String u) {this.username = u;}
    public String getPassword() {return password;} public void setPassword(String p) {this.password = p;}
    public String getEmail() {return email;} public void setEmail(String e) {this.email = e;}
    public String getFullName() {return fullName;} public void setFullName(String fn) {this.fullName = fn;}
    public boolean isEnabled() {return enabled;} public void setEnabled(boolean e) {this.enabled = e;}
    public Set<Role> getRoles() {return roles;} public void setRoles(Set<Role> r) {this.roles = r;}
    @Override public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof User u)) return false; return id != null && id.equals(u.id); }
    @Override public int hashCode() { return Objects.hash(id); }
}
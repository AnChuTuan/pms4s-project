package com.pms4st.pms.entity;

import java.util.HashSet;
import java.util.Set;


public class User {

    private Long id;
    private String username;
    private String password;
    private String email;
    private String fullName;

    private boolean enabled = true;
    private Set<Role> roles = new HashSet<>();

    public User() {}

    public User(Long id, String username, String password, String email, String fullName) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
    }

    // Getter & Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }

    public void addRole(Role role) { this.roles.add(role); }
    public void removeRole(Role role) { this.roles.remove(role); }

    public boolean hasRole(Role role) {
        return this.roles.contains(role);
    }

    public boolean hasRole(String roleName) {
        for (Role role : this.roles) {
            if (role.getName().equals(roleName)) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        return "User{" +
            "id=" + id +
            ", username='" + username + '\'' +
            ", email='" + email + '\'' +
            ", fullName='" + fullName + '\'' +
            ", enabled=" + enabled +
            ", roles=" + roles +
            '}';
    }
}

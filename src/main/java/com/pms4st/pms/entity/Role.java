package com.pms4st.pms.entity;
import jakarta.persistence.*;
import java.util.Objects;

@Entity @Table(name = "roles")
public class Role {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Integer id;
    @Column(nullable = false, unique = true) private String name;
    // --- Basic Getters/Setters/Constructors ---
    public Role() {}
    public Integer getId() {return id;} public void setId(Integer id) {this.id = id;}
    public String getName() {return name;} public void setName(String n) {this.name = n;}
    @Override public boolean equals(Object o) { if (o == this) return true; if (!(o instanceof Role r)) return false; return name != null && name.equals(r.name); }
    @Override public int hashCode() { return Objects.hash(name); }
}
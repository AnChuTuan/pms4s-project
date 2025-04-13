package com.pms4st.pms.entity;

import java.util.Objects;

public class Role {

    private Integer id;
    private String name; // Ví dụ: ROLE_ADMIN, ROLE_USER

    public Role() {}

    public Role(String name) {this.name = name;}
    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    // Đảm bảo Set<Role> hoạt động đúng
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;
        Role role = (Role) o;
        return Objects.equals(name, role.name);
    }

    public int hashCode() {return Objects.hash(name);}
    public String toString() {return this.name;}
}

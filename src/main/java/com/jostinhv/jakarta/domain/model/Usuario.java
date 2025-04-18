package com.jostinhv.jakarta.domain.model;


import com.jostinhv.jakarta.domain.annotations.Domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Domain
public class Usuario {

    private int id;
    private String username;
    private String password;
    private Set<Rol> roles = new HashSet<>();

    public Usuario() {
    }

    public Usuario(int id, String username, String password, Set<Rol> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Rol> getRoles() {
        return roles;
    }

    public void setRoles(Set<Rol> roles) {
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRolesAsString() {
        List<String> rolesAsString = new ArrayList<>();
        for (Rol rol : roles) {
            rolesAsString.add(rol.getNombre());
        }
        return rolesAsString;
    }
}

package com.jostinhv.jakarta.domain.model;

import com.jostinhv.jakarta.domain.annotations.Domain;

@Domain
public class Rol {

    private int id;
    private String nombre;

    public Rol(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

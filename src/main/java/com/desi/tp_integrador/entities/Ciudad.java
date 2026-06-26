package com.desi.tp_integrador.entities;

import jakarta.persistence.*;

@Entity
public class Ciudad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String codigoPostal;

    private Boolean eliminado = false;

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public Boolean getEliminado() {
        return eliminado;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
    }
}
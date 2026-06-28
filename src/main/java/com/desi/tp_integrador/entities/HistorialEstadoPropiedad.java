package com.desi.tp_integrador.entities;

import java.time.LocalDateTime;

import com.desi.tp_integrador.enums.EstadoPropiedad;

import jakarta.persistence.*;

@Entity
public class HistorialEstadoPropiedad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Propiedad propiedad;

    @Enumerated(EnumType.STRING)
    private EstadoPropiedad estado;

    private LocalDateTime fechaCambio;

    public Long getId() {
        return id;
    }

    public Propiedad getPropiedad() {
        return propiedad;
    }

    public EstadoPropiedad getEstado() {
        return estado;
    }

    public LocalDateTime getFechaCambio() {
        return fechaCambio;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPropiedad(Propiedad propiedad) {
        this.propiedad = propiedad;
    }

    public void setEstado(EstadoPropiedad estado) {
        this.estado = estado;
    }

    public void setFechaCambio(LocalDateTime fechaCambio) {
        this.fechaCambio = fechaCambio;
    }
}

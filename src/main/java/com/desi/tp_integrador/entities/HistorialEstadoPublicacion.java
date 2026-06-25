package com.desi.tp_integrador.entities;

import java.time.LocalDateTime;

import com.desi.tp_integrador.enums.EstadoPublicacion;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class HistorialEstadoPublicacion {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "publicacion_id", nullable = false)
    private Publicacion publicacion;

    @Enumerated(EnumType.STRING)
    private EstadoPublicacion estadoAnterior;

    @Enumerated(EnumType.STRING)
    private EstadoPublicacion estadoNuevo;

    private LocalDateTime fechaCambio = LocalDateTime.now();

    public Long getId() {
        return id;
    }

    public Publicacion getPublicacion() {
        return publicacion;
    }

    public EstadoPublicacion getEstadoAnterior() {
        return estadoAnterior;
    }

    public EstadoPublicacion getEstadoNuevo() {
        return estadoNuevo;
    }

    public LocalDateTime getFechaCambio() {
        return fechaCambio;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPublicacion(Publicacion publicacion) {
        this.publicacion = publicacion;
    }

    public void setEstadoAnterior(EstadoPublicacion estadoAnterior) {
        this.estadoAnterior = estadoAnterior;
    }

    public void setEstadoNuevo(EstadoPublicacion estadoNuevo) {
        this.estadoNuevo = estadoNuevo;
    }

    public void setFechaCambio(LocalDateTime fechaCambio) {
        this.fechaCambio = fechaCambio;
    }

}

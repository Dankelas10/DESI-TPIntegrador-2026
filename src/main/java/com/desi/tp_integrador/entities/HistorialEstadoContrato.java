package com.desi.tp_integrador.entities;

import java.time.LocalDateTime;

import com.desi.tp_integrador.enums.EstadoContrato;

import jakarta.persistence.*;

@Entity
public class HistorialEstadoContrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Contrato contrato;

    @Enumerated(EnumType.STRING)
    private EstadoContrato estado;

    private LocalDateTime fechaCambio;

    public Long getId() {
        return id;
    }

    public Contrato getContrato() {
        return contrato;
    }

    public EstadoContrato getEstado() {
        return estado;
    }

    public LocalDateTime getFechaCambio() {
        return fechaCambio;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }

    public void setEstado(EstadoContrato estado) {
        this.estado = estado;
    }

    public void setFechaCambio(LocalDateTime fechaCambio) {
        this.fechaCambio = fechaCambio;
    }
}
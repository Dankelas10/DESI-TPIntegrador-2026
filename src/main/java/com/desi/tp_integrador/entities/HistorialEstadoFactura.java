package com.desi.tp_integrador.entities;

import java.time.LocalDateTime;

import com.desi.tp_integrador.enums.EstadoFactura;

import jakarta.persistence.*;

@Entity
public class HistorialEstadoFactura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Factura factura;

    @Enumerated(EnumType.STRING)
    private EstadoFactura estado;

    private LocalDateTime fechaCambio;

    public Long getId() {
        return id;
    }

    public Factura getFactura() {
        return factura;
    }

    public EstadoFactura getEstado() {
        return estado;
    }

    public LocalDateTime getFechaCambio() {
        return fechaCambio;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public void setEstado(EstadoFactura estado) {
        this.estado = estado;
    }

    public void setFechaCambio(LocalDateTime fechaCambio) {
        this.fechaCambio = fechaCambio;
    }
}

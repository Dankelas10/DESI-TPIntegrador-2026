package com.desi.tp_integrador.entities;

import java.time.LocalDate;

import com.desi.tp_integrador.enums.EstadoContrato;

import jakarta.persistence.*;

@Entity
public class Contrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Propiedad propiedad;

    @ManyToOne
    private Persona inquilino;

    private LocalDate fechaInicio;

    private Integer duracionMeses;

    private Double importeMensual;

    private Integer diaVencimientoMensual;

    private String descripcion;

    @Enumerated(EnumType.STRING)
    private EstadoContrato estado = EstadoContrato.BORRADOR;

    private Boolean eliminado = false;

    public Long getId() { return id; }
    public Propiedad getPropiedad() { return propiedad; }
    public Persona getInquilino() { return inquilino; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public Integer getDuracionMeses() { return duracionMeses; }
    public Double getImporteMensual() { return importeMensual; }
    public Integer getDiaVencimientoMensual() { return diaVencimientoMensual; }
    public String getDescripcion() { return descripcion; }
    public EstadoContrato getEstado() { return estado; }
    public Boolean getEliminado() { return eliminado; }

    public void setId(Long id) { this.id = id; }
    public void setPropiedad(Propiedad propiedad) { this.propiedad = propiedad; }
    public void setInquilino(Persona inquilino) { this.inquilino = inquilino; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public void setDuracionMeses(Integer duracionMeses) { this.duracionMeses = duracionMeses; }
    public void setImporteMensual(Double importeMensual) { this.importeMensual = importeMensual; }
    public void setDiaVencimientoMensual(Integer diaVencimientoMensual) { this.diaVencimientoMensual = diaVencimientoMensual; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setEstado(EstadoContrato estado) { this.estado = estado; }
    public void setEliminado(Boolean eliminado) { this.eliminado = eliminado; }
}
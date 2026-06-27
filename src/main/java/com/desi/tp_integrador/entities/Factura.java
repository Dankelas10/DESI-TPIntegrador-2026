package com.desi.tp_integrador.entities;

import java.time.LocalDate;

import com.desi.tp_integrador.enums.EstadoFactura;
import com.desi.tp_integrador.enums.MedioPago;

import jakarta.persistence.*;

@Entity
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Contrato contrato;

    private String concepto;

    private LocalDate fechaEmision;

    private LocalDate fechaVencimiento;

    private Double importe;

    @Enumerated(EnumType.STRING)
    private EstadoFactura estado = EstadoFactura.PENDIENTE;

    // Datos de pago (atributos de la factura, NO una entidad Pago)
    private LocalDate fechaPago;

    @Enumerated(EnumType.STRING)
    private MedioPago medioPago;

    private Double importePagado;

    private Double interesPagado;

    private Boolean eliminado = false;

    public Long getId() { return id; }
    public Contrato getContrato() { return contrato; }
    public String getConcepto() { return concepto; }
    public LocalDate getFechaEmision() { return fechaEmision; }
    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public Double getImporte() { return importe; }
    public EstadoFactura getEstado() { return estado; }
    public LocalDate getFechaPago() { return fechaPago; }
    public MedioPago getMedioPago() { return medioPago; }
    public Double getImportePagado() { return importePagado; }
    public Double getInteresPagado() { return interesPagado; }
    public Boolean getEliminado() { return eliminado; }

    public void setId(Long id) { this.id = id; }
    public void setContrato(Contrato contrato) { this.contrato = contrato; }
    public void setConcepto(String concepto) { this.concepto = concepto; }
    public void setFechaEmision(LocalDate fechaEmision) { this.fechaEmision = fechaEmision; }
    public void setFechaVencimiento(LocalDate fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }
    public void setImporte(Double importe) { this.importe = importe; }
    public void setEstado(EstadoFactura estado) { this.estado = estado; }
    public void setFechaPago(LocalDate fechaPago) { this.fechaPago = fechaPago; }
    public void setMedioPago(MedioPago medioPago) { this.medioPago = medioPago; }
    public void setImportePagado(Double importePagado) { this.importePagado = importePagado; }
    public void setInteresPagado(Double interesPagado) { this.interesPagado = interesPagado; }
    public void setEliminado(Boolean eliminado) { this.eliminado = eliminado; }
}

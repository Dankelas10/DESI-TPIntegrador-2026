package com.desi.tp_integrador.entities;

import java.math.BigDecimal;
import jakarta.persistence.Column;
import java.time.LocalDate;

import com.desi.tp_integrador.enums.EstadoPublicacion;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Column;

@Entity
public class Publicacion {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "propiedad_id", nullable = false)
	private Propiedad propiedad;

	private BigDecimal precioMensual;

	private String condiciones;

	@Column(length = 500)
	private String descripcion;

	private LocalDate fechaPublicacion;

	@Enumerated(EnumType.STRING)
	private EstadoPublicacion estado = EstadoPublicacion.ACTIVA;

	private Boolean eliminado = false;

	    // Getters

	    public Long getId() {
	        return id;
	    }

	    public Propiedad getPropiedad() {
	        return propiedad;
	    }

	    public BigDecimal getPrecioMensual() {
	        return precioMensual;
	    }

	    public String getCondiciones() {
	        return condiciones;
	    }

	    public String getDescripcion() {
	        return descripcion;
	    }

	    public LocalDate getFechaPublicacion() {
	        return fechaPublicacion;
	    }

	    public EstadoPublicacion getEstado() {
	        return estado;
	    }

	    public Boolean getEliminado() {
	        return eliminado;
	    }

	    // Setters

	    public void setId(Long id) {
	        this.id = id;
	    }

	    public void setPropiedad(Propiedad propiedad) {
	        this.propiedad = propiedad;
	    }

	    public void setPrecioMensual(BigDecimal precioMensual) {
	        this.precioMensual = precioMensual;
	    }

	    public void setCondiciones(String condiciones) {
	        this.condiciones = condiciones;
	    }

	    public void setDescripcion(String descripcion) {
	        this.descripcion = descripcion;
	    }

	    public void setFechaPublicacion(LocalDate fechaPublicacion) {
	        this.fechaPublicacion = fechaPublicacion;
	    }

	    public void setEstado(EstadoPublicacion estado) {
	        this.estado = estado;
	    }

	    public void setEliminado(Boolean eliminado) {
	        this.eliminado = eliminado;
	    }

}

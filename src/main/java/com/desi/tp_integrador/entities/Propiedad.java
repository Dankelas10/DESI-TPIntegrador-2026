package com.desi.tp_integrador.entities;

import com.desi.tp_integrador.enums.EstadoPropiedad;
import com.desi.tp_integrador.enums.TipoPropiedad;

import jakarta.persistence.*;

@Entity
public class Propiedad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String direccion;

    @ManyToOne
    private Ciudad ciudad;

    @Enumerated(EnumType.STRING)
    private TipoPropiedad tipoPropiedad;

    private Integer cantidadAmbientes;

    private Double metrosCuadrados;

    private String descripcion;

    @Enumerated(EnumType.STRING)
    private EstadoPropiedad estado = EstadoPropiedad.DISPONIBLE;

    @ManyToOne
    private Persona propietario;

    private Boolean eliminado = false;

    public Long getId() { return id; }
    public String getDireccion() { return direccion; }
    public Ciudad getCiudad() { return ciudad; }
    public TipoPropiedad getTipoPropiedad() { return tipoPropiedad; }
    public Integer getCantidadAmbientes() { return cantidadAmbientes; }
    public Double getMetrosCuadrados() { return metrosCuadrados; }
    public String getDescripcion() { return descripcion; }
    public EstadoPropiedad getEstado() { return estado; }
    public Persona getPropietario() { return propietario; }
    public Boolean getEliminado() { return eliminado; }

    public void setId(Long id) { this.id = id; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public void setCiudad(Ciudad ciudad) { this.ciudad = ciudad; }
    public void setTipoPropiedad(TipoPropiedad tipoPropiedad) { this.tipoPropiedad = tipoPropiedad; }
    public void setCantidadAmbientes(Integer cantidadAmbientes) { this.cantidadAmbientes = cantidadAmbientes; }
    public void setMetrosCuadrados(Double metrosCuadrados) { this.metrosCuadrados = metrosCuadrados; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setEstado(EstadoPropiedad estado) { this.estado = estado; }
    public void setPropietario(Persona propietario) { this.propietario = propietario; }
    public void setEliminado(Boolean eliminado) { this.eliminado = eliminado; }
}
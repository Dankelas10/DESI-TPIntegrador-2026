package com.desi.tp_integrador.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.desi.tp_integrador.entities.HistorialEstadoPropiedad;
import com.desi.tp_integrador.entities.Propiedad;

public interface HistorialEstadoPropiedadRepository extends JpaRepository<HistorialEstadoPropiedad, Long> {

    List<HistorialEstadoPropiedad> findByPropiedad(Propiedad propiedad);
}

package com.desi.tp_integrador.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.desi.tp_integrador.entities.Contrato;
import com.desi.tp_integrador.entities.Persona;
import com.desi.tp_integrador.entities.Propiedad;
import com.desi.tp_integrador.enums.EstadoContrato;

public interface ContratoRepository extends JpaRepository<Contrato, Long> {

    List<Contrato> findByEliminadoFalse();

    List<Contrato> findByPropiedadAndEliminadoFalse(Propiedad propiedad);

    List<Contrato> findByInquilinoAndEliminadoFalse(Persona inquilino);

    List<Contrato> findByEstadoAndEliminadoFalse(EstadoContrato estado);

    List<Contrato> findByFechaInicioAndEliminadoFalse(LocalDate fechaInicio);

    boolean existsByPropiedadAndEstadoAndEliminadoFalse(Propiedad propiedad, EstadoContrato estado);

    @Query("""
           SELECT c FROM Contrato c
           WHERE c.eliminado = false
           AND (:propiedadId IS NULL OR c.propiedad.id = :propiedadId)
           AND (:inquilinoId IS NULL OR c.inquilino.id = :inquilinoId)
           AND (:estado IS NULL OR c.estado = :estado)
           AND (:fechaInicio IS NULL OR c.fechaInicio = :fechaInicio)
           """)
    List<Contrato> buscarConFiltros(
            @Param("propiedadId") Long propiedadId,
            @Param("inquilinoId") Long inquilinoId,
            @Param("estado") EstadoContrato estado,
            @Param("fechaInicio") LocalDate fechaInicio
    );
}
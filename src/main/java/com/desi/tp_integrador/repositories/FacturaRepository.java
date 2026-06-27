package com.desi.tp_integrador.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.desi.tp_integrador.entities.Contrato;
import com.desi.tp_integrador.entities.Factura;
import com.desi.tp_integrador.enums.EstadoFactura;

public interface FacturaRepository extends JpaRepository<Factura, Long> {

    List<Factura> findByEliminadoFalse();

    List<Factura> findByContratoAndEliminadoFalse(Contrato contrato);

    List<Factura> findByEstadoAndEliminadoFalse(EstadoFactura estado);

    @Query("""
           SELECT f FROM Factura f
           WHERE f.eliminado = false
           AND (:contratoId IS NULL OR f.contrato.id = :contratoId)
           AND (:propiedadId IS NULL OR f.contrato.propiedad.id = :propiedadId)
           AND (:inquilinoId IS NULL OR f.contrato.inquilino.id = :inquilinoId)
           AND (:estado IS NULL OR f.estado = :estado)
           AND (:vencimientoDesde IS NULL OR f.fechaVencimiento >= :vencimientoDesde)
           AND (:vencimientoHasta IS NULL OR f.fechaVencimiento <= :vencimientoHasta)
           """)
    List<Factura> buscarConFiltros(
            @Param("contratoId") Long contratoId,
            @Param("propiedadId") Long propiedadId,
            @Param("inquilinoId") Long inquilinoId,
            @Param("estado") EstadoFactura estado,
            @Param("vencimientoDesde") LocalDate vencimientoDesde,
            @Param("vencimientoHasta") LocalDate vencimientoHasta
    );
}

package com.desi.tp_integrador.repositories;

import java.util.List;

import com.desi.tp_integrador.enums.TipoPropiedad;
import org.springframework.data.jpa.repository.JpaRepository;

import com.desi.tp_integrador.entities.Propiedad;
import com.desi.tp_integrador.enums.EstadoPropiedad;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PropiedadRepository extends JpaRepository<Propiedad, Long> {

    List<Propiedad> findByEliminadoFalse();

    List<Propiedad> findByEstadoAndEliminadoFalse(EstadoPropiedad estado);
    
    List<Propiedad> findByEliminadoFalseAndEstado(EstadoPropiedad estado);

    // Para validar duplicados por direccion + ciudad entre propiedades no eliminadas
    boolean existsByDireccionAndCiudadIdAndEliminadoFalse(String direccion, Long ciudadId);

    @Query("""
           SELECT p FROM Propiedad p
           WHERE p.eliminado = false
           AND (:direccion IS NULL OR LOWER(p.direccion) LIKE LOWER(CONCAT('%', :direccion, '%')))
           AND (:ciudadId IS NULL OR p.ciudad.id = :ciudadId)
           AND (:tipo IS NULL OR p.tipoPropiedad = :tipo)
           AND (:estado IS NULL OR p.estado = :estado)
           """)
    List<Propiedad> buscarConFiltros(
            @Param("direccion") String direccion,
            @Param("ciudadId") Long ciudadId,
            @Param("tipo") TipoPropiedad tipo,
            @Param("estado") EstadoPropiedad estado
    );
}
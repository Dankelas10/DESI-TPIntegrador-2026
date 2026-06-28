package com.desi.tp_integrador.repositories;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.desi.tp_integrador.entities.Publicacion;
import com.desi.tp_integrador.enums.EstadoPublicacion;
import com.desi.tp_integrador.entities.Propiedad;

public interface PublicacionRepository extends JpaRepository<Publicacion, Long> {
	
    List<Publicacion> findByEliminadoFalse();

    boolean existsByPropiedadAndEstadoAndEliminadoFalse(
            Propiedad propiedad,
            EstadoPublicacion estado
    );

    @Query("""
           SELECT p FROM Publicacion p
           WHERE p.eliminado = false
           AND (:propiedadId IS NULL OR p.propiedad.id = :propiedadId)
           AND (:ciudadId IS NULL OR p.propiedad.ciudad.id = :ciudadId)
           AND (:estado IS NULL OR p.estado = :estado)
           AND (:precioMin IS NULL OR p.precioMensual >= :precioMin)
           AND (:precioMax IS NULL OR p.precioMensual <= :precioMax)
           """)
    List<Publicacion> filtrarPublicaciones(
            Long propiedadId,
            Long ciudadId,
            EstadoPublicacion estado,
            BigDecimal precioMin,
            BigDecimal precioMax
    );

}
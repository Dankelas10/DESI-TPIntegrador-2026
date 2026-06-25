package com.desi.tp_integrador.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.desi.tp_integrador.entities.Propiedad;
import com.desi.tp_integrador.entities.Publicacion;
import com.desi.tp_integrador.enums.EstadoPublicacion;

public interface PublicacionRepository extends JpaRepository<Publicacion, Long> {
	
    List<Publicacion> findByEliminadoFalse();

    List<Publicacion> findByEstado(EstadoPublicacion estado);

    boolean existsByPropiedadAndEstadoAndEliminadoFalse(
            Propiedad propiedad,
            EstadoPublicacion estado
    );

}

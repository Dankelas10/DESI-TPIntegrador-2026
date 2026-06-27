package com.desi.tp_integrador.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.desi.tp_integrador.entities.Publicacion;
import com.desi.tp_integrador.enums.EstadoPublicacion;
import com.desi.tp_integrador.entities.Propiedad;

public interface PublicacionRepository extends JpaRepository<Publicacion, Long> {
	
    List<Publicacion> findByEliminadoFalse();

    boolean existsByPropiedadAndEstadoAndEliminadoFalse(
            Propiedad propiedad,
            EstadoPublicacion estado
    );

}

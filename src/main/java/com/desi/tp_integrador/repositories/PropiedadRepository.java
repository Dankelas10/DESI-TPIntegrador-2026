package com.desi.tp_integrador.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.desi.tp_integrador.entities.Propiedad;
import com.desi.tp_integrador.enums.EstadoPropiedad;

public interface PropiedadRepository extends JpaRepository<Propiedad, Long> {
	
	List<Propiedad> findByEliminadoFalse();

    List<Propiedad> findByEliminadoFalseAndEstado(EstadoPropiedad estado);


}

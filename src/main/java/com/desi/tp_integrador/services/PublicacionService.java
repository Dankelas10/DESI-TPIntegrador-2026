package com.desi.tp_integrador.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.desi.tp_integrador.entities.Publicacion;
import com.desi.tp_integrador.enums.EstadoPublicacion;

public interface PublicacionService {
	
    List<Publicacion> listarTodas();

    Optional<Publicacion> buscarPorId(Long id);
    
    Publicacion obtenerPorId(Long id);

    Publicacion guardar(Publicacion publicacion, Long propiedadId);

    Publicacion actualizar(Publicacion publicacion);

    List<Publicacion> filtrarPublicaciones(
            Long propiedadId,
            Long ciudadId,
            EstadoPublicacion estado,
            BigDecimal precioMin,
            BigDecimal precioMax
    );
  
    void eliminar(Long id);

}
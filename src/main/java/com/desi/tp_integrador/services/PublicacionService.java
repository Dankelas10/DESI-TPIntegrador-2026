package com.desi.tp_integrador.services;

import java.util.List;
import java.util.Optional;

import com.desi.tp_integrador.entities.Publicacion;

public interface PublicacionService {
	
    List<Publicacion> listarTodas();

    Optional<Publicacion> buscarPorId(Long id);

    Publicacion guardar(Publicacion publicacion);

    Publicacion actualizar(Publicacion publicacion);

    void eliminar(Long id);

}

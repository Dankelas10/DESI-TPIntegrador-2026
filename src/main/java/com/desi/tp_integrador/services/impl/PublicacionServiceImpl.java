package com.desi.tp_integrador.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desi.tp_integrador.entities.Publicacion;
import com.desi.tp_integrador.repositories.PublicacionRepository;
import com.desi.tp_integrador.services.PublicacionService;

@Service
public class PublicacionServiceImpl implements PublicacionService {
	
		@Autowired
	    private PublicacionRepository publicacionRepository;

	    @Override
	    public List<Publicacion> listarTodas() {
	        return publicacionRepository.findAll();
	    }

	    @Override
	    public Optional<Publicacion> buscarPorId(Long id) {
	        return publicacionRepository.findById(id);
	    }

	    @Override
	    public Publicacion guardar(Publicacion publicacion) {
	        return publicacionRepository.save(publicacion);
	    }

	    @Override
	    public Publicacion actualizar(Publicacion publicacion) {
	        return publicacionRepository.save(publicacion);
	    }

	    @Override
	    public void eliminar(Long id) {
	        publicacionRepository.deleteById(id);
	    }

}

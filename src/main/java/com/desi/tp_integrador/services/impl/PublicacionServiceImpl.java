package com.desi.tp_integrador.services.impl;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desi.tp_integrador.entities.Publicacion;
import com.desi.tp_integrador.repositories.PublicacionRepository;
import com.desi.tp_integrador.services.PublicacionService;
import com.desi.tp_integrador.entities.Propiedad;
import com.desi.tp_integrador.enums.EstadoPropiedad;
import com.desi.tp_integrador.enums.EstadoPublicacion;
import com.desi.tp_integrador.repositories.PropiedadRepository;

@Service
public class PublicacionServiceImpl implements PublicacionService {
	
		@Autowired
	    private PublicacionRepository publicacionRepository;
		
		@Autowired
		private PropiedadRepository propiedadRepository;

	    @Override
	    public List<Publicacion> listarTodas() {
	        return publicacionRepository.findAll();
	    }

	    @Override
	    public Optional<Publicacion> buscarPorId(Long id) {
	        return publicacionRepository.findById(id);
	    }

	    @Override
	    public Publicacion guardar(Publicacion publicacion, Long propiedadId) {

	        Propiedad propiedad = propiedadRepository.findById(propiedadId)
	                .orElseThrow(() -> new RuntimeException("La propiedad no existe"));

	        if (Boolean.TRUE.equals(propiedad.getEliminado())) {
	            throw new RuntimeException("No se puede publicar una propiedad eliminada");
	        }

	        if (propiedad.getEstado() != EstadoPropiedad.DISPONIBLE) {
	            throw new RuntimeException("La propiedad no está disponible");
	        }

	        boolean yaTienePublicacionActiva =
	                publicacionRepository.existsByPropiedadAndEstadoAndEliminadoFalse(
	                        propiedad,
	                        EstadoPublicacion.ACTIVA
	                );

	        if (yaTienePublicacionActiva) {
	            throw new RuntimeException("La propiedad ya tiene una publicación activa");
	        }

	        publicacion.setPropiedad(propiedad);
	        publicacion.setFechaPublicacion(LocalDate.now());
	        publicacion.setEstado(EstadoPublicacion.ACTIVA);
	        publicacion.setEliminado(false);

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

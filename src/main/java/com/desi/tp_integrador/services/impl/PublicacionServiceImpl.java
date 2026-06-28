package com.desi.tp_integrador.services.impl;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desi.tp_integrador.entities.Publicacion;
import com.desi.tp_integrador.entities.Propiedad;
import com.desi.tp_integrador.enums.EstadoPropiedad;
import com.desi.tp_integrador.enums.EstadoPublicacion;
import com.desi.tp_integrador.repositories.PropiedadRepository;
import com.desi.tp_integrador.repositories.PublicacionRepository;
import com.desi.tp_integrador.services.PublicacionService;

@Service
public class PublicacionServiceImpl implements PublicacionService {

    @Autowired
    private PublicacionRepository publicacionRepository;

    @Autowired
    private PropiedadRepository propiedadRepository;

    // Lista solamente las publicaciones no eliminadas.
    @Override
    public List<Publicacion> listarTodas() {
        return publicacionRepository.findByEliminadoFalse();
    }

    // Busca una publicacion por id y devuelve Optional.
    @Override
    public Optional<Publicacion> buscarPorId(Long id) {
        return publicacionRepository.findById(id);
    }

    // Guarda una nueva publicacion asociada a una propiedad existente.
    @Override
    public Publicacion guardar(Publicacion publicacion, Long propiedadId) {

        // Busca la propiedad seleccionada en el formulario.
        // Si no existe, se lanza una excepcion para evitar guardar una publicacion invalida.
        Propiedad propiedad = propiedadRepository.findById(propiedadId)
                .orElseThrow(() -> new RuntimeException("La propiedad no existe"));

        // Regla de negocio: no se puede publicar una propiedad eliminada.
        if (Boolean.TRUE.equals(propiedad.getEliminado())) {
            throw new RuntimeException("No se puede publicar una propiedad eliminada");
        }

        // Regla de negocio: solo se pueden publicar propiedades disponibles.
        if (propiedad.getEstado() != EstadoPropiedad.DISPONIBLE) {
            throw new RuntimeException("La propiedad no esta disponible");
        }

        // Regla de negocio: no puede existir mas de una publicacion activa
        // para la misma propiedad.
        boolean yaTienePublicacionActiva =
                publicacionRepository.existsByPropiedadAndEstadoAndEliminadoFalse(
                        propiedad,
                        EstadoPublicacion.ACTIVA
                );

        if (yaTienePublicacionActiva) {
            throw new RuntimeException("La propiedad ya tiene una publicacion activa");
        }

        // Valores iniciales de una nueva publicacion.
        publicacion.setPropiedad(propiedad);
        publicacion.setFechaPublicacion(LocalDate.now());
        publicacion.setEstado(EstadoPublicacion.ACTIVA);
        publicacion.setEliminado(false);

        return publicacionRepository.save(publicacion);
    }

    // Obtiene una publicacion por id. Si no existe, lanza una excepcion.
    @Override
    public Publicacion obtenerPorId(Long id) {
        return publicacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La publicacion no existe"));
    }

    // Actualiza una publicacion existente.
    // La propiedad asociada no se cambia para evitar inconsistencias historicas.
    @Override
    public Publicacion actualizar(Publicacion publicacion) {

        Publicacion publicacionExistente = publicacionRepository.findById(publicacion.getId())
                .orElseThrow(() -> new RuntimeException("La publicacion no existe"));

        // Regla de negocio: el precio mensual debe ser positivo.
        if (publicacion.getPrecioMensual() == null || publicacion.getPrecioMensual().signum() <= 0) {
            throw new RuntimeException("El precio mensual debe ser un numero positivo");
        }

        // Si la publicacion quiere quedar activa, se validan reglas adicionales.
        if (publicacion.getEstado() == EstadoPublicacion.ACTIVA) {

            // Solo se puede activar si la propiedad asociada esta disponible.
            if (publicacionExistente.getPropiedad().getEstado() != EstadoPropiedad.DISPONIBLE) {
                throw new RuntimeException("Solo se puede activar una publicacion si la propiedad esta disponible");
            }

            // Verifica si ya existe una publicacion activa para esa propiedad.
            boolean yaTienePublicacionActiva =
                    publicacionRepository.existsByPropiedadAndEstadoAndEliminadoFalse(
                            publicacionExistente.getPropiedad(),
                            EstadoPublicacion.ACTIVA
                    );

            // Si la publicacion actual no estaba activa y ya existe otra activa,
            // no se permite activarla.
            if (yaTienePublicacionActiva && publicacionExistente.getEstado() != EstadoPublicacion.ACTIVA) {
                throw new RuntimeException("Ya existe otra publicacion activa para esta propiedad");
            }
        }

        // Campos que se pueden modificar.
        publicacionExistente.setPrecioMensual(publicacion.getPrecioMensual());
        publicacionExistente.setDescripcion(publicacion.getDescripcion());

        // Las condiciones solo se modifican si la publicacion no estaba finalizada.
        if (publicacionExistente.getEstado() != EstadoPublicacion.FINALIZADA) {
            publicacionExistente.setCondiciones(publicacion.getCondiciones());
        }

        publicacionExistente.setEstado(publicacion.getEstado());

        return publicacionRepository.save(publicacionExistente);
    }

    // Aplica los filtros del listado de publicaciones.
    @Override
    public List<Publicacion> filtrarPublicaciones(
            Long propiedadId,
            Long ciudadId,
            EstadoPublicacion estado,
            BigDecimal precioMin,
            BigDecimal precioMax) {

        return publicacionRepository.filtrarPublicaciones(
                propiedadId,
                ciudadId,
                estado,
                precioMin,
                precioMax
        );
    }

    // Eliminacion logica de una publicacion.
    @Override
    public void eliminar(Long id) {

        Publicacion publicacion = publicacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La publicacion no existe"));

        // Regla de negocio: solo se pueden eliminar publicaciones activas.
        if (publicacion.getEstado() != EstadoPublicacion.ACTIVA) {
            throw new RuntimeException("Solo se pueden eliminar publicaciones activas");
        }

        // No se borra fisicamente el registro.
        // Solo se marca como eliminado para que no aparezca en el listado.
        publicacion.setEliminado(true);

        publicacionRepository.save(publicacion);
    }

}
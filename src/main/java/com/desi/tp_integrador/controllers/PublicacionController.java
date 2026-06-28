package com.desi.tp_integrador.controllers;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

import com.desi.tp_integrador.entities.Publicacion;
import com.desi.tp_integrador.enums.EstadoPropiedad;
import com.desi.tp_integrador.enums.EstadoPublicacion;
import com.desi.tp_integrador.repositories.CiudadRepository;
import com.desi.tp_integrador.repositories.PropiedadRepository;
import com.desi.tp_integrador.services.PublicacionService;

@Controller
public class PublicacionController {

    @Autowired
    private PublicacionService publicacionService;

    @Autowired
    private PropiedadRepository propiedadRepository;

    @Autowired
    private CiudadRepository ciudadRepository;

    // Muestra el listado de publicaciones.
    // Tambien recibe parametros opcionales para aplicar filtros.
    @GetMapping("/publicaciones")
    public String listarPublicaciones(
            @RequestParam(required = false) Long propiedadId,
            @RequestParam(required = false) Long ciudadId,
            @RequestParam(required = false) EstadoPublicacion estado,
            @RequestParam(required = false) BigDecimal precioMin,
            @RequestParam(required = false) BigDecimal precioMax,
            Model model) {

        // Obtiene publicaciones aplicando los filtros ingresados por el usuario.
        model.addAttribute("publicaciones",
                publicacionService.filtrarPublicaciones(
                        propiedadId,
                        ciudadId,
                        estado,
                        precioMin,
                        precioMax));

        // Datos necesarios para completar los combos del formulario de filtros.
        model.addAttribute("propiedades", propiedadRepository.findAll());
        model.addAttribute("ciudades", ciudadRepository.findByEliminadoFalse());
        model.addAttribute("estadosPublicacion", EstadoPublicacion.values());

        return "publicaciones";
    }

    // Abre el formulario para crear una nueva publicacion.
    @GetMapping("/publicaciones/nueva")
    public String nuevaPublicacion(Model model) {

        model.addAttribute("publicacion", new Publicacion());

        // Solo se muestran propiedades disponibles y no eliminadas.
        model.addAttribute("propiedades",
                propiedadRepository.findByEliminadoFalseAndEstado(EstadoPropiedad.DISPONIBLE));

        model.addAttribute("estadosPublicacion", EstadoPublicacion.values());

        return "publicacion-form";
    }

    // Abre el formulario para editar una publicacion existente.
    @GetMapping("/publicaciones/editar/{id}")
    public String editarPublicacion(@PathVariable Long id, Model model) {

        Publicacion publicacion = publicacionService.obtenerPorId(id);

        model.addAttribute("publicacion", publicacion);
        model.addAttribute("propiedades", propiedadRepository.findAll());
        model.addAttribute("estadosPublicacion", EstadoPublicacion.values());

        return "publicacion-form";
    }

    // Ejecuta la eliminacion logica de una publicacion.
    @GetMapping("/publicaciones/eliminar/{id}")
    public String eliminarPublicacion(@PathVariable Long id) {

        publicacionService.eliminar(id);

        return "redirect:/publicaciones";
    }

    // Guarda una nueva publicacion.
    @PostMapping("/publicaciones/guardar")
    public String guardarPublicacion(@ModelAttribute Publicacion publicacion,
                                     @RequestParam Long propiedadId,
                                     Model model) {

        try {
            publicacionService.guardar(publicacion, propiedadId);
            return "redirect:/publicaciones";

        } catch (RuntimeException e) {

            // Si ocurre una regla de negocio, se muestra el mensaje en el formulario
            // en lugar de mostrar una pagina de error.
            model.addAttribute("error", e.getMessage());
            model.addAttribute("publicacion", publicacion);
            model.addAttribute("propiedades",
                    propiedadRepository.findByEliminadoFalseAndEstado(EstadoPropiedad.DISPONIBLE));
            model.addAttribute("estadosPublicacion", EstadoPublicacion.values());

            return "publicacion-form";
        }
    }

    // Actualiza una publicacion existente.
    @PostMapping("/publicaciones/actualizar")
    public String actualizarPublicacion(@ModelAttribute Publicacion publicacion,
                                        Model model) {

        try {
            publicacionService.actualizar(publicacion);
            return "redirect:/publicaciones";

        } catch (RuntimeException e) {

            // Si la actualizacion no cumple una regla de negocio,
            // se vuelve al formulario mostrando el mensaje de error.
            Publicacion publicacionExistente = publicacionService.obtenerPorId(publicacion.getId());

            model.addAttribute("error", e.getMessage());
            model.addAttribute("publicacion", publicacionExistente);
            model.addAttribute("propiedades", propiedadRepository.findAll());
            model.addAttribute("estadosPublicacion", EstadoPublicacion.values());

            return "publicacion-form";
        }
    }

}
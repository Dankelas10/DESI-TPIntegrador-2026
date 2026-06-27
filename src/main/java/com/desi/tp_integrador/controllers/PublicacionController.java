package com.desi.tp_integrador.controllers;

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

import com.desi.tp_integrador.enums.EstadoPropiedad;
import com.desi.tp_integrador.enums.EstadoPublicacion;
import com.desi.tp_integrador.entities.Publicacion;
import com.desi.tp_integrador.repositories.PropiedadRepository;
import com.desi.tp_integrador.services.PublicacionService;

@Controller
public class PublicacionController {
	
    @Autowired
    private PublicacionService publicacionService;
    
    @Autowired
    private PropiedadRepository propiedadRepository;

    @GetMapping("/publicaciones")
    public String listarPublicaciones(Model model) {
        model.addAttribute("publicaciones", publicacionService.listarTodas());
        return "publicaciones";
    }
    
    @GetMapping("/publicaciones/nueva")
    public String nuevaPublicacion(Model model) {
        model.addAttribute("publicacion", new Publicacion());
        model.addAttribute("propiedades", propiedadRepository.findByEliminadoFalseAndEstado(EstadoPropiedad.DISPONIBLE));
        model.addAttribute("estadosPublicacion", EstadoPublicacion.values());
        return "publicacion-form";
    }
    
    @GetMapping("/publicaciones/editar/{id}")
    public String editarPublicacion(@PathVariable Long id, Model model) {
        Publicacion publicacion = publicacionService.obtenerPorId(id);

        model.addAttribute("publicacion", publicacion);
        model.addAttribute("propiedades", propiedadRepository.findAll());
        model.addAttribute("estadosPublicacion", EstadoPublicacion.values());

        return "publicacion-form";
    }
    
    @GetMapping("/publicaciones/eliminar/{id}")
    public String eliminarPublicacion(@PathVariable Long id) {
        publicacionService.eliminar(id);
        return "redirect:/publicaciones";
    }
    
    @PostMapping("/publicaciones/guardar")
    public String guardarPublicacion(@ModelAttribute Publicacion publicacion,
                                     @RequestParam Long propiedadId) {

        publicacionService.guardar(publicacion, propiedadId);
        return "redirect:/publicaciones";
    }

    @PostMapping("/publicaciones/actualizar")
    public String actualizarPublicacion(@ModelAttribute Publicacion publicacion) {

        publicacionService.actualizar(publicacion);
        return "redirect:/publicaciones";
    }
    
}

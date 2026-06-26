package com.desi.tp_integrador.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.desi.tp_integrador.enums.EstadoPropiedad;
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
        return "publicacion-form";
    }
    
    @PostMapping("/publicaciones/guardar")
    public String guardarPublicacion(@ModelAttribute Publicacion publicacion,
                                     @RequestParam Long propiedadId) {

        publicacionService.guardar(publicacion, propiedadId);
        return "redirect:/publicaciones";
    }
}

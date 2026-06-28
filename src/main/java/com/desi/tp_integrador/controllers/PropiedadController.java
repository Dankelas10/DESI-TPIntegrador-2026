package com.desi.tp_integrador.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.desi.tp_integrador.entities.Propiedad;
import com.desi.tp_integrador.enums.EstadoPropiedad;
import com.desi.tp_integrador.enums.TipoPropiedad;
import com.desi.tp_integrador.repositories.CiudadRepository;
import com.desi.tp_integrador.repositories.PersonaRepository;
import com.desi.tp_integrador.services.PropiedadService;

@Controller
@RequestMapping("/propiedades")
public class PropiedadController {

    @Autowired
    private PropiedadService propiedadService;

    @Autowired
    private CiudadRepository ciudadRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @GetMapping
    public String listar(
            @RequestParam(required = false) String direccion,
            @RequestParam(required = false) Long ciudadId,
            @RequestParam(required = false) TipoPropiedad tipo,
            @RequestParam(required = false) EstadoPropiedad estado,
            Model model) {

        model.addAttribute("propiedades",
                propiedadService.listarConFiltros(direccion, ciudadId, tipo, estado));

        model.addAttribute("ciudades", ciudadRepository.findByEliminadoFalse());
        model.addAttribute("tipos", TipoPropiedad.values());
        model.addAttribute("estados", EstadoPropiedad.values());

        model.addAttribute("direccion", direccion);
        model.addAttribute("ciudadId", ciudadId);
        model.addAttribute("tipoSeleccionado", tipo);
        model.addAttribute("estadoSeleccionado", estado);

        return "propiedades/listado";
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        model.addAttribute("propiedad", new Propiedad());
        model.addAttribute("ciudades", ciudadRepository.findByEliminadoFalse());
        model.addAttribute("personas", personaRepository.findByEliminadoFalse());
        model.addAttribute("tipos", TipoPropiedad.values());
        model.addAttribute("estados", EstadoPropiedad.values());
        return "propiedades/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("propiedad", propiedadService.buscarPorId(id));
        model.addAttribute("ciudades", ciudadRepository.findByEliminadoFalse());
        model.addAttribute("personas", personaRepository.findByEliminadoFalse());
        model.addAttribute("tipos", TipoPropiedad.values());
        model.addAttribute("estados", EstadoPropiedad.values());
        return "propiedades/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Propiedad propiedad,
                          RedirectAttributes redirectAttributes) {

        try {
            propiedadService.guardar(propiedad);
            redirectAttributes.addFlashAttribute("mensajeExito", "Propiedad guardada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
        }

        return "redirect:/propiedades";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id,
                           RedirectAttributes redirectAttributes) {

        try {
            propiedadService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Propiedad eliminada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
        }

        return "redirect:/propiedades";
    }
}

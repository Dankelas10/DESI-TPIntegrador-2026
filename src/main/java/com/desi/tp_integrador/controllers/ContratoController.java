package com.desi.tp_integrador.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.desi.tp_integrador.entities.Contrato;
import com.desi.tp_integrador.enums.EstadoContrato;
import com.desi.tp_integrador.enums.EstadoPropiedad;
import com.desi.tp_integrador.repositories.PersonaRepository;
import com.desi.tp_integrador.repositories.PropiedadRepository;
import com.desi.tp_integrador.services.ContratoService;

@Controller
@RequestMapping("/contratos")
public class ContratoController {

    @Autowired
    private ContratoService contratoService;

    @Autowired
    private PropiedadRepository propiedadRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @GetMapping
    public String listar(
            @RequestParam(required = false) Long propiedadId,
            @RequestParam(required = false) Long inquilinoId,
            @RequestParam(required = false) EstadoContrato estado,
            @RequestParam(required = false) java.time.LocalDate fechaInicio,
            Model model) {

        model.addAttribute("contratos",
                contratoService.listarConFiltros(propiedadId, inquilinoId, estado, fechaInicio));

        model.addAttribute("propiedades", propiedadRepository.findByEliminadoFalse());
        model.addAttribute("personas", personaRepository.findByEliminadoFalse());
        model.addAttribute("estados", EstadoContrato.values());

        model.addAttribute("propiedadId", propiedadId);
        model.addAttribute("inquilinoId", inquilinoId);
        model.addAttribute("estadoSeleccionado", estado);
        model.addAttribute("fechaInicio", fechaInicio);

        return "contratos/listado";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("contrato", new Contrato());
        model.addAttribute("propiedades", propiedadRepository.findByEstadoAndEliminadoFalse(EstadoPropiedad.DISPONIBLE));
        model.addAttribute("personas", personaRepository.findByEliminadoFalse());
        model.addAttribute("estados", EstadoContrato.values());
        return "contratos/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Contrato contrato,
                          RedirectAttributes redirectAttributes) {

        try {
            if (contrato.getId() != null) {
                Contrato anterior = contratoService.buscarPorId(contrato.getId());

                if (contrato.getFechaInicio() == null) {
                    contrato.setFechaInicio(anterior.getFechaInicio());
                }

                if (contrato.getPropiedad() == null) {
                    contrato.setPropiedad(anterior.getPropiedad());
                }

                if (contrato.getInquilino() == null) {
                    contrato.setInquilino(anterior.getInquilino());
                }
            }

            contratoService.guardar(contrato);
            redirectAttributes.addFlashAttribute("mensajeExito", "Contrato guardado correctamente.");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
        }

        return "redirect:/contratos";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("contrato", contratoService.buscarPorId(id));
        model.addAttribute("propiedades", propiedadRepository.findByEliminadoFalse());
        model.addAttribute("personas", personaRepository.findByEliminadoFalse());
        model.addAttribute("estados", EstadoContrato.values());
        return "contratos/formulario";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id,
                           RedirectAttributes redirectAttributes) {

        try {
            contratoService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensajeExito",
                    "Contrato eliminado correctamente.");

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute("mensajeError",
                    e.getMessage());
        }

        return "redirect:/contratos";
    }
}
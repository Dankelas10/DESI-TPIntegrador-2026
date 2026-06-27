package com.desi.tp_integrador.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.desi.tp_integrador.entities.Factura;
import com.desi.tp_integrador.enums.EstadoContrato;
import com.desi.tp_integrador.enums.EstadoFactura;
import com.desi.tp_integrador.enums.MedioPago;
import com.desi.tp_integrador.repositories.ContratoRepository;
import com.desi.tp_integrador.repositories.PersonaRepository;
import com.desi.tp_integrador.repositories.PropiedadRepository;
import com.desi.tp_integrador.services.FacturaService;

@Controller
@RequestMapping("/facturas")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;

    @Autowired
    private ContratoRepository contratoRepository;

    @Autowired
    private PropiedadRepository propiedadRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @GetMapping
    public String listar(
            @RequestParam(required = false) Long contratoId,
            @RequestParam(required = false) Long propiedadId,
            @RequestParam(required = false) Long inquilinoId,
            @RequestParam(required = false) EstadoFactura estado,
            @RequestParam(required = false) java.time.LocalDate vencimientoDesde,
            @RequestParam(required = false) java.time.LocalDate vencimientoHasta,
            Model model) {

        model.addAttribute("facturas",
                facturaService.listarConFiltros(contratoId, propiedadId, inquilinoId,
                        estado, vencimientoDesde, vencimientoHasta));

        model.addAttribute("contratos", contratoRepository.findByEliminadoFalse());
        model.addAttribute("propiedades", propiedadRepository.findByEliminadoFalse());
        model.addAttribute("personas", personaRepository.findByEliminadoFalse());
        model.addAttribute("estados", EstadoFactura.values());

        model.addAttribute("contratoId", contratoId);
        model.addAttribute("propiedadId", propiedadId);
        model.addAttribute("inquilinoId", inquilinoId);
        model.addAttribute("estadoSeleccionado", estado);
        model.addAttribute("vencimientoDesde", vencimientoDesde);
        model.addAttribute("vencimientoHasta", vencimientoHasta);

        return "facturas/listado";
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        model.addAttribute("factura", new Factura());
        // Solo contratos activos y no eliminados (HU 4.1)
        model.addAttribute("contratos",
                contratoRepository.findByEstadoAndEliminadoFalse(EstadoContrato.ACTIVO));
        model.addAttribute("estados", EstadoFactura.values());
        model.addAttribute("mediosPago", MedioPago.values());
        return "facturas/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("factura", facturaService.buscarPorId(id));
        model.addAttribute("contratos", contratoRepository.findByEliminadoFalse());
        model.addAttribute("estados", EstadoFactura.values());
        model.addAttribute("mediosPago", MedioPago.values());
        return "facturas/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Factura factura,
                          RedirectAttributes redirectAttributes) {

        try {
            facturaService.guardar(factura);
            redirectAttributes.addFlashAttribute("mensajeExito", "Factura guardada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
        }

        return "redirect:/facturas";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id,
                           RedirectAttributes redirectAttributes) {

        try {
            facturaService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Factura eliminada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
        }

        return "redirect:/facturas";
    }
}

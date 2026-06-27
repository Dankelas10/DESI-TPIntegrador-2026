package com.desi.tp_integrador.services.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desi.tp_integrador.entities.Factura;
import com.desi.tp_integrador.entities.HistorialEstadoFactura;
import com.desi.tp_integrador.enums.EstadoContrato;
import com.desi.tp_integrador.enums.EstadoFactura;
import com.desi.tp_integrador.repositories.FacturaRepository;
import com.desi.tp_integrador.repositories.HistorialEstadoFacturaRepository;
import com.desi.tp_integrador.services.FacturaService;

@Service
public class FacturaServiceImpl implements FacturaService {

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private HistorialEstadoFacturaRepository historialRepository;

    @Override
    public List<Factura> listar() {
        return facturaRepository.findByEliminadoFalse();
    }

    @Override
    public List<Factura> listarConFiltros(Long contratoId, Long propiedadId, Long inquilinoId,
                                          EstadoFactura estado, LocalDate vencimientoDesde, LocalDate vencimientoHasta) {
        return facturaRepository.buscarConFiltros(contratoId, propiedadId, inquilinoId,
                estado, vencimientoDesde, vencimientoHasta);
    }

    @Override
    public Factura buscarPorId(Long id) {
        return facturaRepository.findById(id).orElse(null);
    }

    @Override
    public Factura guardar(Factura factura) {

        Factura facturaAnterior = null;

        if (factura.getId() != null) {
            facturaAnterior = buscarPorId(factura.getId());
        }

        validarDatosBasicos(factura, facturaAnterior);

        if (facturaAnterior == null) {
            // ALTA (HU 4.1)
            validarAlta(factura);

            if (factura.getEstado() == null) {
                factura.setEstado(EstadoFactura.PENDIENTE);
            }
        } else {
            // MODIFICACIÓN (HU 4.2)
            validarModificacion(facturaAnterior, factura);

            // El contrato asociado no se puede modificar una vez creada la factura.
            factura.setContrato(facturaAnterior.getContrato());
        }

        validarDatosPago(factura);

        Factura facturaGuardada = facturaRepository.save(factura);

        if (facturaAnterior == null || facturaAnterior.getEstado() != factura.getEstado()) {
            registrarHistorial(facturaGuardada);
        }

        return facturaGuardada;
    }

    @Override
    public void eliminar(Long id) {
        Factura factura = buscarPorId(id);

        if (factura == null) {
            throw new RuntimeException("No se encontró la factura.");
        }

        // HU 4.3: no se puede eliminar una factura pagada.
        if (factura.getEstado() == EstadoFactura.PAGADA) {
            throw new RuntimeException("No se puede eliminar una factura pagada.");
        }

        factura.setEliminado(true);
        facturaRepository.save(factura);
    }

    // ---------- Validaciones ----------

    private void validarDatosBasicos(Factura factura, Factura facturaAnterior) {

        // En el alta el contrato es obligatorio. En la modificación se conserva el original,
        // por eso solo se exige cuando es una factura nueva.
        if (facturaAnterior == null && factura.getContrato() == null) {
            throw new RuntimeException("Debe seleccionar un contrato.");
        }

        if (factura.getConcepto() == null || factura.getConcepto().isBlank()) {
            throw new RuntimeException("Debe ingresar el concepto facturado.");
        }

        if (factura.getFechaEmision() == null) {
            throw new RuntimeException("Debe ingresar una fecha de emisión válida.");
        }

        if (factura.getFechaVencimiento() == null) {
            throw new RuntimeException("Debe ingresar una fecha de vencimiento válida.");
        }

        if (factura.getFechaVencimiento().isBefore(factura.getFechaEmision())) {
            throw new RuntimeException("La fecha de vencimiento debe ser igual o posterior a la fecha de emisión.");
        }

        if (factura.getImporte() == null || factura.getImporte() <= 0) {
            throw new RuntimeException("El importe debe ser un número positivo.");
        }
    }

    private void validarAlta(Factura factura) {
        // HU 4.1: solo se factura sobre contratos activos y no eliminados.
        if (factura.getContrato() == null) {
            throw new RuntimeException("Debe seleccionar un contrato.");
        }

        if (Boolean.TRUE.equals(factura.getContrato().getEliminado())) {
            throw new RuntimeException("No se puede facturar sobre un contrato eliminado.");
        }

        if (factura.getContrato().getEstado() != EstadoContrato.ACTIVO) {
            throw new RuntimeException("Solo se pueden generar facturas para contratos activos.");
        }
    }

    private void validarModificacion(Factura anterior, Factura nueva) {

        // HU 4.2: no se puede modificar una factura anulada ni pagada.
        if (anterior.getEstado() == EstadoFactura.ANULADA) {
            throw new RuntimeException("No se puede modificar una factura anulada.");
        }

        if (anterior.getEstado() == EstadoFactura.PAGADA) {
            throw new RuntimeException("No se puede modificar una factura pagada.");
        }

        validarCambioEstado(anterior.getEstado(), nueva.getEstado());
    }

    private void validarCambioEstado(EstadoFactura anterior, EstadoFactura nuevo) {

        if (anterior == nuevo) {
            return;
        }

        boolean transicionValida =
                (anterior == EstadoFactura.PENDIENTE && nuevo == EstadoFactura.PAGADA) ||
                (anterior == EstadoFactura.PENDIENTE && nuevo == EstadoFactura.VENCIDA) ||
                (anterior == EstadoFactura.PENDIENTE && nuevo == EstadoFactura.ANULADA) ||
                (anterior == EstadoFactura.VENCIDA  && nuevo == EstadoFactura.PAGADA) ||
                (anterior == EstadoFactura.VENCIDA  && nuevo == EstadoFactura.ANULADA);

        if (!transicionValida) {
            throw new RuntimeException("Transición de estado no permitida: " + anterior + " -> " + nuevo + ".");
        }
    }

    private void validarDatosPago(Factura factura) {

        if (factura.getEstado() == EstadoFactura.PAGADA) {
            // Si pasa a PAGADA, los datos de pago son obligatorios.
            if (factura.getFechaPago() == null) {
                throw new RuntimeException("Debe ingresar la fecha de pago.");
            }
            if (factura.getMedioPago() == null) {
                throw new RuntimeException("Debe seleccionar el medio de pago.");
            }
            if (factura.getImportePagado() == null || factura.getImportePagado() <= 0) {
                throw new RuntimeException("El importe pagado debe ser un número positivo.");
            }
        } else {
            // Si NO está pagada, los datos de pago deben quedar vacíos.
            // (Además, no se permiten datos de pago en una factura anulada.)
            factura.setFechaPago(null);
            factura.setMedioPago(null);
            factura.setImportePagado(null);
            factura.setInteresPagado(null);
        }
    }

    private void registrarHistorial(Factura factura) {
        HistorialEstadoFactura historial = new HistorialEstadoFactura();
        historial.setFactura(factura);
        historial.setEstado(factura.getEstado());
        historial.setFechaCambio(LocalDateTime.now());

        historialRepository.save(historial);
    }
}

package com.desi.tp_integrador.services.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desi.tp_integrador.entities.Contrato;
import com.desi.tp_integrador.entities.HistorialEstadoContrato;
import com.desi.tp_integrador.enums.EstadoContrato;
import com.desi.tp_integrador.enums.EstadoPropiedad;
import com.desi.tp_integrador.repositories.ContratoRepository;
import com.desi.tp_integrador.repositories.HistorialEstadoContratoRepository;
import com.desi.tp_integrador.repositories.PropiedadRepository;
import com.desi.tp_integrador.services.ContratoService;

@Service
public class ContratoServiceImpl implements ContratoService {

    @Autowired
    private ContratoRepository contratoRepository;

    @Autowired
    private PropiedadRepository propiedadRepository;

    @Autowired
    private HistorialEstadoContratoRepository historialRepository;

    @Override
    public List<Contrato> listar() {
        return contratoRepository.findByEliminadoFalse();
    }
    @Override
    public List<Contrato> listarConFiltros(Long propiedadId, Long inquilinoId, EstadoContrato estado, LocalDate fechaInicio) {
        return contratoRepository.buscarConFiltros(propiedadId, inquilinoId, estado, fechaInicio);
    }
    @Override
    public Contrato buscarPorId(Long id) {
        return contratoRepository.findById(id).orElse(null);
    }

    @Override
    public Contrato guardar(Contrato contrato) {

        validarDatosBasicos(contrato);

        Contrato contratoAnterior = null;

        if (contrato.getId() != null) {
            contratoAnterior = buscarPorId(contrato.getId());
        }

        if (contrato.getEstado() == EstadoContrato.ACTIVO) {
            validarActivacionContrato(contrato);
            contrato.getPropiedad().setEstado(EstadoPropiedad.ALQUILADA);
            propiedadRepository.save(contrato.getPropiedad());
        }

        if (contratoAnterior != null) {
            validarCambioEstado(contratoAnterior, contrato);

            if (contratoAnterior.getEstado() == EstadoContrato.ACTIVO
                    && contrato.getEstado() != EstadoContrato.ACTIVO) {
                contrato.getPropiedad().setEstado(EstadoPropiedad.DISPONIBLE);
                propiedadRepository.save(contrato.getPropiedad());
            }
        }

        Contrato contratoGuardado = contratoRepository.save(contrato);

        if (contratoAnterior == null || contratoAnterior.getEstado() != contrato.getEstado()) {
            registrarHistorial(contratoGuardado);
        }

        return contratoGuardado;
    }

    @Override
    public void eliminar(Long id) {
        Contrato contrato = buscarPorId(id);

        if (contrato == null) {
            throw new RuntimeException("No se encontró el contrato.");
        }

        if (contrato.getEstado() != EstadoContrato.BORRADOR) {
            throw new RuntimeException("Solo se pueden eliminar contratos en estado BORRADOR.");
        }
        if (contrato.getEstado() == EstadoContrato.BORRADOR 
                && contrato.getPropiedad() != null 
                && contrato.getPropiedad().getEstado() == EstadoPropiedad.ALQUILADA) {
            contrato.getPropiedad().setEstado(EstadoPropiedad.DISPONIBLE);
            propiedadRepository.save(contrato.getPropiedad());
        }
        contrato.setEliminado(true);
        contratoRepository.save(contrato);
    }

    private void validarDatosBasicos(Contrato contrato) {

        if (contrato.getPropiedad() == null) {
            throw new RuntimeException("Debe seleccionar una propiedad.");
        }

        if (contrato.getInquilino() == null) {
            throw new RuntimeException("Debe seleccionar un inquilino.");
        }

        if (contrato.getFechaInicio() == null) {
            throw new RuntimeException("Debe ingresar una fecha de inicio válida.");
        }

        if (contrato.getDuracionMeses() == null || contrato.getDuracionMeses() <= 0) {
            throw new RuntimeException("La duración en meses debe ser positiva.");
        }

        if (contrato.getImporteMensual() == null || contrato.getImporteMensual() <= 0) {
            throw new RuntimeException("El importe mensual debe ser positivo.");
        }

        if (contrato.getDiaVencimientoMensual() == null 
                || contrato.getDiaVencimientoMensual() < 1 
                || contrato.getDiaVencimientoMensual() > 31) {
            throw new RuntimeException("El día de vencimiento mensual debe estar entre 1 y 31.");
        }

        if (contrato.getEstado() == null) {
            contrato.setEstado(EstadoContrato.BORRADOR);
        }
    }

    private void validarActivacionContrato(Contrato contrato) {

        if (contrato.getPropiedad().getEstado() != EstadoPropiedad.DISPONIBLE) {
            throw new RuntimeException("No se puede activar el contrato porque la propiedad no está disponible.");
        }

        boolean existeActivo = contratoRepository.existsByPropiedadAndEstadoAndEliminadoFalse(
                contrato.getPropiedad(), EstadoContrato.ACTIVO);

        if (existeActivo) {
            throw new RuntimeException("La propiedad ya tiene un contrato activo.");
        }
    }

    private void validarCambioEstado(Contrato anterior, Contrato nuevo) {

        if (anterior.getEstado() == EstadoContrato.ACTIVO 
                && nuevo.getEstado() == EstadoContrato.BORRADOR) {
            throw new RuntimeException("No se puede volver de ACTIVO a BORRADOR. Debe finalizar o rescindir el contrato.");
        }

        if (anterior.getEstado() == EstadoContrato.FINALIZADO 
                && nuevo.getEstado() == EstadoContrato.ACTIVO) {
            throw new RuntimeException("No se puede volver de FINALIZADO a ACTIVO.");
        }

        if (anterior.getEstado() == EstadoContrato.RESCINDIDO 
                && nuevo.getEstado() == EstadoContrato.ACTIVO) {
            throw new RuntimeException("No se puede volver de RESCINDIDO a ACTIVO.");
        }
    }

    private void registrarHistorial(Contrato contrato) {
        HistorialEstadoContrato historial = new HistorialEstadoContrato();
        historial.setContrato(contrato);
        historial.setEstado(contrato.getEstado());
        historial.setFechaCambio(LocalDateTime.now());

        historialRepository.save(historial);
    }
}
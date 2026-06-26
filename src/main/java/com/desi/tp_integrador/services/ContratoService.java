package com.desi.tp_integrador.services;

import java.time.LocalDate;
import java.util.List;

import com.desi.tp_integrador.entities.Contrato;
import com.desi.tp_integrador.enums.EstadoContrato;

public interface ContratoService {

    List<Contrato> listar();

    List<Contrato> listarConFiltros(Long propiedadId, Long inquilinoId, EstadoContrato estado, LocalDate fechaInicio);

    Contrato buscarPorId(Long id);

    Contrato guardar(Contrato contrato);

    void eliminar(Long id);
}
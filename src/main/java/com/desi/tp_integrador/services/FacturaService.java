package com.desi.tp_integrador.services;

import java.time.LocalDate;
import java.util.List;

import com.desi.tp_integrador.entities.Factura;
import com.desi.tp_integrador.enums.EstadoFactura;

public interface FacturaService {

    List<Factura> listar();

    List<Factura> listarConFiltros(Long contratoId, Long propiedadId, Long inquilinoId,
                                   EstadoFactura estado, LocalDate vencimientoDesde, LocalDate vencimientoHasta);

    Factura buscarPorId(Long id);

    Factura guardar(Factura factura);

    void eliminar(Long id);
}

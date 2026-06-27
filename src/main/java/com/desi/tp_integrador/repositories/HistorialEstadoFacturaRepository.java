package com.desi.tp_integrador.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.desi.tp_integrador.entities.Factura;
import com.desi.tp_integrador.entities.HistorialEstadoFactura;

public interface HistorialEstadoFacturaRepository extends JpaRepository<HistorialEstadoFactura, Long> {

    List<HistorialEstadoFactura> findByFactura(Factura factura);
}

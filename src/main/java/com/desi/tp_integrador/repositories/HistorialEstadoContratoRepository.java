package com.desi.tp_integrador.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.desi.tp_integrador.entities.Contrato;
import com.desi.tp_integrador.entities.HistorialEstadoContrato;

public interface HistorialEstadoContratoRepository extends JpaRepository<HistorialEstadoContrato, Long> {

    List<HistorialEstadoContrato> findByContrato(Contrato contrato);
}
package com.desi.tp_integrador.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.desi.tp_integrador.entities.Ciudad;

public interface CiudadRepository extends JpaRepository<Ciudad, Long> {

    List<Ciudad> findByEliminadoFalse();

}
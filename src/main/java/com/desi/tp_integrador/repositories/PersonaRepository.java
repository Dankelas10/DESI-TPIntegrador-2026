package com.desi.tp_integrador.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.desi.tp_integrador.entities.Persona;

public interface PersonaRepository extends JpaRepository<Persona, Long> {

    List<Persona> findByEliminadoFalse();
}
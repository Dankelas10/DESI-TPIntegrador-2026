package com.desi.tp_integrador.services;

import java.util.List;

import com.desi.tp_integrador.entities.Propiedad;
import com.desi.tp_integrador.enums.EstadoPropiedad;
import com.desi.tp_integrador.enums.TipoPropiedad;

public interface PropiedadService {

    List<Propiedad> listar();

    List<Propiedad> listarConFiltros(String direccion, Long ciudadId, TipoPropiedad tipo, EstadoPropiedad estado);

    Propiedad buscarPorId(Long id);

    Propiedad guardar(Propiedad propiedad);

    void eliminar(Long id);
}

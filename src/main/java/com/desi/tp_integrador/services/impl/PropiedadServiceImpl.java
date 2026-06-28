package com.desi.tp_integrador.services.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desi.tp_integrador.entities.HistorialEstadoPropiedad;
import com.desi.tp_integrador.entities.Propiedad;
import com.desi.tp_integrador.enums.EstadoContrato;
import com.desi.tp_integrador.enums.EstadoPropiedad;
import com.desi.tp_integrador.enums.TipoPropiedad;
import com.desi.tp_integrador.repositories.ContratoRepository;
import com.desi.tp_integrador.repositories.HistorialEstadoPropiedadRepository;
import com.desi.tp_integrador.repositories.PropiedadRepository;
import com.desi.tp_integrador.services.PropiedadService;

@Service
public class PropiedadServiceImpl implements PropiedadService {

    @Autowired
    private PropiedadRepository propiedadRepository;

    @Autowired
    private ContratoRepository contratoRepository;

    @Autowired
    private HistorialEstadoPropiedadRepository historialRepository;

    @Override
    public List<Propiedad> listar() {
        return propiedadRepository.findByEliminadoFalse();
    }

    @Override
    public List<Propiedad> listarConFiltros(String direccion, Long ciudadId, TipoPropiedad tipo, EstadoPropiedad estado) {
        String dir = (direccion == null || direccion.isBlank()) ? null : direccion;
        return propiedadRepository.buscarConFiltros(dir, ciudadId, tipo, estado);
    }

    @Override
    public Propiedad buscarPorId(Long id) {
        return propiedadRepository.findById(id).orElse(null);
    }

    @Override
    public Propiedad guardar(Propiedad propiedad) {

        Propiedad propiedadAnterior = null;

        if (propiedad.getId() != null) {
            propiedadAnterior = buscarPorId(propiedad.getId());
        }

        validarDatos(propiedad);
        validarDuplicado(propiedad);

        if (propiedadAnterior == null) {
            // ALTA (HU 1.1): estado por defecto DISPONIBLE si no se indicó otro.
            if (propiedad.getEstado() == null) {
                propiedad.setEstado(EstadoPropiedad.DISPONIBLE);
            }
        } else {
            // MODIFICACIÓN (HU 1.3)
            validarCambioEstado(propiedadAnterior, propiedad);
        }

        Propiedad propiedadGuardada = propiedadRepository.save(propiedad);

        // Registrar historial si es alta o si cambió el estado.
        if (propiedadAnterior == null || propiedadAnterior.getEstado() != propiedad.getEstado()) {
            registrarHistorial(propiedadGuardada);
        }

        return propiedadGuardada;
    }

    @Override
    public void eliminar(Long id) {
        Propiedad propiedad = buscarPorId(id);

        if (propiedad == null) {
            throw new RuntimeException("No se encontró la propiedad.");
        }

        // HU 1.2: no se puede eliminar una propiedad con un contrato activo vigente.
        boolean tieneContratoActivo = contratoRepository
                .existsByPropiedadAndEstadoAndEliminadoFalse(propiedad, EstadoContrato.ACTIVO);

        if (tieneContratoActivo) {
            throw new RuntimeException("No se puede eliminar la propiedad: tiene un contrato activo vigente.");
        }

        // Eliminación lógica: no se pierden los registros históricos asociados.
        propiedad.setEliminado(true);
        propiedadRepository.save(propiedad);
    }

    // ---------- Validaciones ----------

    private void validarDatos(Propiedad propiedad) {

        if (propiedad.getDireccion() == null || propiedad.getDireccion().isBlank()) {
            throw new RuntimeException("Debe ingresar la dirección.");
        }

        if (propiedad.getCiudad() == null) {
            throw new RuntimeException("Debe seleccionar una ciudad.");
        }

        if (propiedad.getTipoPropiedad() == null) {
            throw new RuntimeException("Debe seleccionar el tipo de propiedad.");
        }

        if (propiedad.getCantidadAmbientes() == null || propiedad.getCantidadAmbientes() <= 0) {
            throw new RuntimeException("La cantidad de ambientes debe ser un número entero positivo.");
        }

        if (propiedad.getMetrosCuadrados() == null || propiedad.getMetrosCuadrados() <= 0) {
            throw new RuntimeException("Los metros cuadrados deben ser un número positivo.");
        }

        if (propiedad.getDescripcion() == null || propiedad.getDescripcion().isBlank()) {
            throw new RuntimeException("Debe ingresar la descripción.");
        }

        if (propiedad.getPropietario() == null) {
            throw new RuntimeException("Debe seleccionar un propietario.");
        }
    }

    private void validarDuplicado(Propiedad propiedad) {
        // No puede haber dos propiedades activas con la misma dirección y ciudad (HU 1.1 / 1.3).
        if (propiedad.getCiudad() == null || propiedad.getCiudad().getId() == null) {
            return;
        }

        List<Propiedad> activas = propiedadRepository.findByEliminadoFalse();
        boolean duplicadaConOtra = activas.stream().anyMatch(p ->
                (propiedad.getId() == null || !p.getId().equals(propiedad.getId()))
                && p.getDireccion() != null
                && p.getDireccion().equalsIgnoreCase(propiedad.getDireccion())
                && p.getCiudad() != null
                && p.getCiudad().getId().equals(propiedad.getCiudad().getId()));

        if (duplicadaConOtra) {
            throw new RuntimeException("Ya existe una propiedad activa con la misma dirección y ciudad.");
        }
    }

    private void validarCambioEstado(Propiedad anterior, Propiedad nueva) {
        // HU 1.3: si la propiedad tiene un contrato activo, no se puede pasar a DISPONIBLE o INACTIVA
        // sin finalizar o rescindir el contrato previamente.
        boolean tieneContratoActivo = contratoRepository
                .existsByPropiedadAndEstadoAndEliminadoFalse(anterior, EstadoContrato.ACTIVO);

        boolean pasaADisponibleOInactiva =
                nueva.getEstado() == EstadoPropiedad.DISPONIBLE ||
                nueva.getEstado() == EstadoPropiedad.INACTIVA;

        if (tieneContratoActivo && pasaADisponibleOInactiva) {
            throw new RuntimeException(
                    "La propiedad tiene un contrato activo: finalice o rescinda el contrato antes de cambiar el estado.");
        }
    }

    private void registrarHistorial(Propiedad propiedad) {
        HistorialEstadoPropiedad historial = new HistorialEstadoPropiedad();
        historial.setPropiedad(propiedad);
        historial.setEstado(propiedad.getEstado());
        historial.setFechaCambio(LocalDateTime.now());

        historialRepository.save(historial);
    }
}

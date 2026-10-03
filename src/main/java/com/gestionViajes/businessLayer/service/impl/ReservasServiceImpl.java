package com.gestionViajes.businessLayer.service.impl;

import com.gestionViajes.persistenceLayer.entity.Reservas;
import com.gestionViajes.persistenceLayer.repository.ReservasRepository;
import com.gestionViajes.businessLayer.dto.ReservasDTO;
import com.gestionViajes.businessLayer.service.ReservasService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservasServiceImpl implements ReservasService {

    private final ReservasRepository reservasRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ReservasDTO> listarTodas() {
        return reservasRepository.findAll().stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ReservasDTO obtenerPorId(Long id) {
        Reservas reserva = reservasRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada con ID: " + id));
        return mapearADto(reserva);
    }

    @Override
    @Transactional
    public ReservasDTO crear(ReservasDTO dto) {
        Reservas reserva = new Reservas();
        reserva.setFechaDeReserva(dto.getFechaDeReserva());
        reserva.setEstado(dto.getEstado());
        // Si necesitas asociar el viaje usando dto.getViajeId(), lo enlazarías aquí buscando el viaje por su ID.

        Reservas reservaGuardada = reservasRepository.save(reserva);
        return mapearADto(reservaGuardada);
    }

    @Override
    @Transactional
    public ReservasDTO actualizar(Long id, ReservasDTO dto) {
        Reservas reserva = reservasRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada con ID: " + id));

        reserva.setFechaDeReserva(dto.getFechaDeReserva());
        reserva.setEstado(dto.getEstado());
        // Actualiza la relación o campos adicionales si llegan en el DTO

        Reservas reservaActualizada = reservasRepository.save(reserva);
        return mapearADto(reservaActualizada);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!reservasRepository.existsById(id)) {
            throw new EntityNotFoundException("Reserva no encontrada con ID: " + id);
        }
        reservasRepository.deleteById(id);
    }

    // Método auxiliar para transformar Entidad -> DTO
    private ReservasDTO mapearADto(Reservas reserva) {
        ReservasDTO dto = new ReservasDTO();
        dto.setId(reserva.getId());
        dto.setFechaDeReserva(reserva.getFechaDeReserva());
        dto.setEstado(reserva.getEstado());

        // Opcional: si la relación 'viajes' no es nula, mapeas su ID al DTO
        if (reserva.getViajes() != null) {
            dto.setViajeId(reserva.getViajes().getId());
        }

        return dto;
    }
}
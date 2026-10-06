package com.gestionViajes.businessLayer.service.impl;

import com.gestionViajes.businessLayer.dto.ReservasDTO;
import com.gestionViajes.businessLayer.service.ReservasService;
import com.gestionViajes.persistenceLayer.entity.Reservas;
import com.gestionViajes.persistenceLayer.mapper.ReservasMapper;
import com.gestionViajes.persistenceLayer.repository.ReservasRepository;
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
    private final ReservasMapper reservasMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ReservasDTO> listarTodas() {
        return reservasRepository.findAll().stream()
                .map(reservasMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ReservasDTO obtenerPorId(Long id) {
        Reservas reserva = reservasRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada con ID: " + id));
        return reservasMapper.toDto(reserva);
    }

    @Override
    @Transactional
    public ReservasDTO crear(ReservasDTO dto) {
        Reservas reserva = reservasMapper.toEntity(dto);
        Reservas reservaGuardada = reservasRepository.save(reserva);
        return reservasMapper.toDto(reservaGuardada);
    }

    @Override
    @Transactional
    public ReservasDTO actualizar(Long id, ReservasDTO dto) {
        Reservas reserva = reservasRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reserva no encontrada con ID: " + id));

        reserva.setFechaDeReserva(dto.getFechaDeReserva());
        reserva.setEstado(dto.getEstado());

        Reservas reservaActualizada = reservasRepository.save(reserva);
        return reservasMapper.toDto(reservaActualizada);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!reservasRepository.existsById(id)) {
            throw new EntityNotFoundException("Reserva no encontrada con ID: " + id);
        }
        reservasRepository.deleteById(id);
    }
}
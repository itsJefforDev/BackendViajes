package com.gestionViajes.businessLayer.service.impl;

import com.gestionViajes.businessLayer.dto.ViajesDTO;
import com.gestionViajes.businessLayer.service.ViajesService;
import com.gestionViajes.persistenceLayer.entity.Viajes;
import com.gestionViajes.persistenceLayer.mapper.ViajesMapper;
import com.gestionViajes.persistenceLayer.repository.ViajesRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ViajesServiceImpl implements ViajesService {

    private final ViajesRepository viajesRepository;
    private final ViajesMapper viajesMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ViajesDTO> listarTodos() {
        return viajesRepository.findAll().stream()
                .map(viajesMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ViajesDTO obtenerPorId(Long id) {
        Viajes viajes = viajesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Viaje no encontrado con ID: " + id));
        return viajesMapper.toDto(viajes);
    }

    @Override
    @Transactional
    public ViajesDTO crear(ViajesDTO viajesDTO) {
        Viajes viajes = viajesMapper.toEntity(viajesDTO);
        Viajes viajeGuardado = viajesRepository.save(viajes);
        return viajesMapper.toDto(viajeGuardado);
    }

    @Override
    @Transactional
    public ViajesDTO actualizar(Long id, ViajesDTO viajesDTO) {
        Viajes viajeExistente = viajesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Viaje no encontrado con ID: " + id));

        viajeExistente.setDuracion(viajesDTO.getDuracion());
        viajeExistente.setPrecio(viajesDTO.getPrecio());
        viajeExistente.setFechas_disponibles(viajesDTO.getFechas_disponibles());
        viajeExistente.setDescripcion(viajesDTO.getDescripcion());

        Viajes viajeActualizado = viajesRepository.save(viajeExistente);
        return viajesMapper.toDto(viajeActualizado);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!viajesRepository.existsById(id)) {
            throw new EntityNotFoundException("Viaje no encontrado con ID: " + id);
        }
        viajesRepository.deleteById(id);
    }
}
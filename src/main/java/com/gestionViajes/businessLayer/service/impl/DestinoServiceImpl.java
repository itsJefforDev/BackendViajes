package com.gestionViajes.businessLayer.service.impl;

import com.gestionViajes.businessLayer.dto.DestinoDTO;
import com.gestionViajes.businessLayer.service.DestinoService;
import com.gestionViajes.persistenceLayer.entity.Destino;
import com.gestionViajes.persistenceLayer.mapper.DestinoMapper;
import com.gestionViajes.persistenceLayer.repository.DestinoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DestinoServiceImpl implements DestinoService {

    private final DestinoRepository destinoRepository;
    private final DestinoMapper destinoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<DestinoDTO> listarTodos() {
        return destinoRepository.findAll().stream()
                .map(destinoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DestinoDTO obtenerPorId(Long id) {
        Destino destino = destinoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Destino no encontrado con ID: " + id));
        return destinoMapper.toDto(destino);
    }

    @Override
    @Transactional
    public DestinoDTO crear(DestinoDTO destinoDTO) {
        Destino destino = destinoMapper.toEntity(destinoDTO);
        Destino destinoGuardado = destinoRepository.save(destino);
        return destinoMapper.toDto(destinoGuardado);
    }

    @Override
    @Transactional
    public DestinoDTO actualizar(Long id, DestinoDTO destinoDTO) {
        Destino destinoExistente = destinoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Destino no encontrado con ID: " + id));

        destinoExistente.setNombre(destinoDTO.getNombre());
        destinoExistente.setDescripcion(destinoDTO.getDescripcion());
        destinoExistente.setAtraccionesTuristicas(destinoDTO.getAtraccionesTuristicas());
        destinoExistente.setClima(destinoDTO.getClima());
        destinoExistente.setRecomendaciones(destinoDTO.getRecomendaciones());

        Destino destinoActualizado = destinoRepository.save(destinoExistente);
        return destinoMapper.toDto(destinoActualizado);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!destinoRepository.existsById(id)) {
            throw new EntityNotFoundException("Destino no encontrado con ID: " + id);
        }
        destinoRepository.deleteById(id);
    }
}
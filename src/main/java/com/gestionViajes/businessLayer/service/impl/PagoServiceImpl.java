package com.gestionViajes.businessLayer.service.impl;

import com.gestionViajes.businessLayer.dto.PagoDTO;
import com.gestionViajes.businessLayer.service.PagoService;
import com.gestionViajes.persistenceLayer.entity.Pago;
import com.gestionViajes.persistenceLayer.mapper.PagoMapper;
import com.gestionViajes.persistenceLayer.repository.PagoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PagoServiceImpl implements PagoService {

    private final PagoRepository pagoRepository;
    private final PagoMapper pagoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<PagoDTO> listarTodos() {
        return pagoRepository.findAll().stream()
                .map(pagoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PagoDTO obtenerPorId(Long id) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pago no encontrado con ID: " + id));
        return pagoMapper.toDto(pago);
    }

    @Override
    @Transactional
    public PagoDTO crear(PagoDTO pagoDTO) {
        Pago pago = pagoMapper.toEntity(pagoDTO);
        Pago pagoGuardado = pagoRepository.save(pago);
        return pagoMapper.toDto(pagoGuardado);
    }

    @Override
    @Transactional
    public PagoDTO actualizar(Long id, PagoDTO pagoDTO) {
        Pago pagoExistente = pagoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pago no encontrado con ID: " + id));

        pagoExistente.setMetodoDePago(pagoDTO.getMetodoDePago());
        pagoExistente.setMonto(pagoDTO.getMonto());
        pagoExistente.setFecha(pagoDTO.getFecha());
        pagoExistente.setEstado(pagoDTO.getEstado());
        pagoExistente.setNumeroTransaccion(pagoDTO.getNumeroTransaccion());

        Pago pagoActualizado = pagoRepository.save(pagoExistente);
        return pagoMapper.toDto(pagoActualizado);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!pagoRepository.existsById(id)) {
            throw new EntityNotFoundException("Pago no encontrado con ID: " + id);
        }
        pagoRepository.deleteById(id);
    }
}
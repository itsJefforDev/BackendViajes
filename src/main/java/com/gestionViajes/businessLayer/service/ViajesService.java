package com.gestionViajes.businessLayer.service;

import com.gestionViajes.businessLayer.dto.ViajesDTO;

import java.util.List;

public interface ViajesService {
    List<ViajesDTO> listarTodos();
    ViajesDTO obtenerPorId(Long id);
    ViajesDTO crear(ViajesDTO viajesDTO);
    ViajesDTO actualizar(Long id, ViajesDTO viajesDTO);
    void eliminar(Long id);
}
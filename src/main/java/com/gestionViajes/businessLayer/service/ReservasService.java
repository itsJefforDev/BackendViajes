package com.gestionViajes.businessLayer.service;

import com.gestionViajes.businessLayer.dto.ReservasDTO;

import java.util.List;

public interface ReservasService {
    List<ReservasDTO> listarTodas();
    ReservasDTO obtenerPorId(Long id);
    ReservasDTO crear(ReservasDTO ReservasDTO);
    ReservasDTO actualizar(Long id, ReservasDTO ReservasDTO);
    void eliminar(Long id);
}
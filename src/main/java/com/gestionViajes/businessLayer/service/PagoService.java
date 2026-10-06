package com.gestionViajes.businessLayer.service;

import com.gestionViajes.businessLayer.dto.PagoDTO;

import java.util.List;

public interface PagoService {
    List<PagoDTO> listarTodos();
    PagoDTO obtenerPorId(Long id);
    PagoDTO crear(PagoDTO pagoDTO);
    PagoDTO actualizar(Long id, PagoDTO pagoDTO);
    void eliminar(Long id);
}
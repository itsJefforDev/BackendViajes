package com.gestionViajes.businessLayer.service;

import com.gestionViajes.businessLayer.dto.DestinoDTO;
import java.util.List;

public interface DestinoService {
    List<DestinoDTO> listarTodos();
    DestinoDTO obtenerPorId(Long id);
    DestinoDTO crear(DestinoDTO destinoDTO);
    DestinoDTO actualizar(Long id, DestinoDTO destinoDTO);
    void eliminar(Long id);
}
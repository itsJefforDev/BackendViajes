package com.gestionViajes.businessLayer.service;

import com.gestionViajes.businessLayer.dto.ClienteDTO;
import java.util.List;

public interface ClienteService {
    List<ClienteDTO> listarTodos();
    ClienteDTO obtenerPorId(Long id);
    ClienteDTO crear(ClienteDTO clienteDTO);
    ClienteDTO actualizar(Long id, ClienteDTO clienteDTO);
    void eliminar(Long id);
}
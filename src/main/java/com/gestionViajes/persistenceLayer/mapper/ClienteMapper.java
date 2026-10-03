package com.gestionViajes.persistenceLayer.mapper;

import com.gestionViajes.businessLayer.dto.ClienteDTO;
import com.gestionViajes.persistenceLayer.entity.Cliente;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ClienteMapper {

    public ClienteDTO toDto(Cliente cliente) {
        if (cliente == null) {
            return null;
        }

        ClienteDTO dto = new ClienteDTO();
        dto.setId(cliente.getId());
        dto.setNombre(cliente.getNombre());
        dto.setEmail(cliente.getEmail());
        dto.setTelefono(cliente.getTelefono());
        dto.setDireccion(cliente.getDireccion());

        // Mapear las reservas a sus respectivos IDs si existen
        if (cliente.getReservas() != null) {
            dto.setReservasIds(
                    cliente.getReservas().stream()
                            .map(reservas -> reservas.getId())
                            .collect(Collectors.toList())
            );
        }

        return dto;
    }

    public Cliente toEntity(ClienteDTO dto) {
        if (dto == null) {
            return null;
        }

        Cliente cliente = new Cliente();
        cliente.setId(dto.getId());
        cliente.setNombre(dto.getNombre());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefono(dto.getTelefono());
        cliente.setDireccion(dto.getDireccion());
        // Nota: La lista de reservas suele gestionarse desde el lado de la entidad Reservas o mediante servicios específicos.

        return cliente;
    }
}
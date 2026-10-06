package com.gestionViajes.persistenceLayer.mapper;

import com.gestionViajes.businessLayer.dto.ViajesDTO;
import com.gestionViajes.persistenceLayer.entity.Viajes;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ViajesMapper {

    public ViajesDTO toDto(Viajes viajes) {
        if (viajes == null) {
            return null;
        }

        ViajesDTO dto = new ViajesDTO();
        dto.setId(viajes.getId());
        dto.setDuracion(viajes.getDuracion());
        dto.setPrecio(viajes.getPrecio());
        dto.setFechas_disponibles(viajes.getFechas_disponibles());
        dto.setDescripcion(viajes.getDescripcion());

        // Mapear la lista de reservas a IDs si existe
        if (viajes.getReservas() != null) {
            dto.setReservasIds(
                    viajes.getReservas().stream()
                            .map(reservas -> reservas.getId())
                            .collect(Collectors.toList())
            );
        }

        // Mapear el destino a su ID si existe
        if (viajes.getDestino() != null) {
            dto.setDestinoId(viajes.getDestino().getId());
        }

        return dto;
    }

    public Viajes toEntity(ViajesDTO dto) {
        if (dto == null) {
            return null;
        }

        Viajes viajes = new Viajes();
        viajes.setId(dto.getId());
        viajes.setDuracion(dto.getDuracion());
        viajes.setPrecio(dto.getPrecio());
        viajes.setFechas_disponibles(dto.getFechas_disponibles());
        viajes.setDescripcion(dto.getDescripcion());
        // Las relaciones (reservas y destino) se gestionan desde sus propios servicios o mappers

        return viajes;
    }
}
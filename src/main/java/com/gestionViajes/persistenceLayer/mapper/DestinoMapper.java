package com.gestionViajes.persistenceLayer.mapper;

import com.gestionViajes.businessLayer.dto.DestinoDTO;
import com.gestionViajes.persistenceLayer.entity.Destino;
import org.springframework.stereotype.Component;

@Component
public class DestinoMapper {

    public DestinoDTO toDto(Destino destino) {
        if (destino == null) {
            return null;
        }
        DestinoDTO dto = new DestinoDTO();
        dto.setId(destino.getId());
        dto.setNombre(destino.getNombre());
        dto.setDescripcion(destino.getDescripcion());
        dto.setAtraccionesTuristicas(destino.getAtraccionesTuristicas());
        dto.setClima(destino.getClima());
        dto.setRecomendaciones(destino.getRecomendaciones());
        return dto;
    }

    public Destino toEntity(DestinoDTO dto) {
        if (dto == null) {
            return null;
        }
        Destino destino = new Destino();
        destino.setId(dto.getId());
        destino.setNombre(dto.getNombre());
        destino.setDescripcion(dto.getDescripcion());
        destino.setAtraccionesTuristicas(dto.getAtraccionesTuristicas());
        destino.setClima(dto.getClima());
        destino.setRecomendaciones(dto.getRecomendaciones());
        return destino;
    }
}
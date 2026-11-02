package com.gestionViajes.persistenceLayer.mapper;

import com.gestionViajes.businessLayer.dto.ReservasDTO;
import com.gestionViajes.persistenceLayer.entity.Reservas;
import org.springframework.stereotype.Component;

@Component
public class ReservasMapper {

    public ReservasDTO toDto(Reservas reserva) {
        if (reserva == null) {
            return null;
        }

        ReservasDTO dto = new ReservasDTO();
        dto.setId(reserva.getId());
        dto.setFechaDeReserva(reserva.getFechaDeReserva());
        dto.setEstado(reserva.getEstado());

        if (reserva.getViajes() != null) {
            dto.setViajeId(reserva.getViajes().getId());
        }

        if (reserva.getClientes() != null) {
            dto.setClienteId(reserva.getClientes().getId());
        }

        return dto;
    }

    public Reservas toEntity(ReservasDTO dto) {
        if (dto == null) {
            return null;
        }

        Reservas reserva = new Reservas();
        reserva.setId(dto.getId());
        reserva.setFechaDeReserva(dto.getFechaDeReserva());
        reserva.setEstado(dto.getEstado());
        // relaciones (cliente y viajes) se asignan desde el service consultando los repositorios

        return reserva;
    }
}
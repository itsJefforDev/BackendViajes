package com.gestionViajes.persistenceLayer.mapper;

import com.gestionViajes.businessLayer.dto.PagoDTO;
import com.gestionViajes.persistenceLayer.entity.Pago;
import org.springframework.stereotype.Component;

@Component
public class PagoMapper {

    public PagoDTO toDto(Pago pago) {
        if (pago == null) {
            return null;
        }

        PagoDTO dto = new PagoDTO();
        dto.setId(pago.getId());
        dto.setMetodoDePago(pago.getMetodoDePago());
        dto.setMonto(pago.getMonto());
        dto.setFecha(pago.getFecha());
        dto.setEstado(pago.getEstado());
        dto.setNumeroTransaccion(pago.getNumeroTransaccion());

        return dto;
    }

    public Pago toEntity(PagoDTO dto) {
        if (dto == null) {
            return null;
        }

        Pago pago = new Pago();
        pago.setId(dto.getId());
        pago.setMetodoDePago(dto.getMetodoDePago());
        pago.setMonto(dto.getMonto());
        pago.setFecha(dto.getFecha());
        pago.setEstado(dto.getEstado());
        pago.setNumeroTransaccion(dto.getNumeroTransaccion());

        return pago;
    }
}
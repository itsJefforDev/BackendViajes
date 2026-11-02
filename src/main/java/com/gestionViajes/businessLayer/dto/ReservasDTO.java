package com.gestionViajes.businessLayer.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReservasDTO {
    private Long id;
    private LocalDateTime fechaDeReserva;
    private Boolean estado;
    private Long viajeId;
    private Long clienteId;
}
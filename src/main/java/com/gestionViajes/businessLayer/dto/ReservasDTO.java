package com.gestionViajes.businessLayer.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReservasDTO {
    private Long id;
    private LocalDateTime fechaDeReserva;
    private Boolean estado;
    private Long viajeId; // Útil al crear/actualizar si necesitas asociarlo al viaje
}
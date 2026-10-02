package com.gestionViajes.businessLayer.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class PagoDTO {
    private Long id;
    private String metodoDePago;
    private BigDecimal monto;
    private LocalDateTime fecha;
    private String estado;
    private String numeroTransaccion;

}

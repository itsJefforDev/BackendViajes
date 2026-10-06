package com.gestionViajes.persistenceLayer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "pagos")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String metodoDePago;
    private BigDecimal monto;
    private LocalDateTime fecha;
    private String estado;
    private String numeroTransaccion;

    @OneToOne
    @JoinColumn(name = "reserva_id")
    private Reservas reservas;
}

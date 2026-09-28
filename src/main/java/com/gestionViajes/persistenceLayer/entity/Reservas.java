package com.gestionViajes.persistenceLayer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fechaDeReserva;

    private Boolean estado;

    @OneToOne(mappedBy = "reserva", fetch = FetchType.LAZY)
    private Viajes viajes;
}
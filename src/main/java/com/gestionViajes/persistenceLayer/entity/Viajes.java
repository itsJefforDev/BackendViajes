package com.gestionViajes.persistenceLayer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "viajes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Viajes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date duracion;

    private double precio;
    private Date fechas_disponibles;
    private String descripcion;

    @OneToMany(mappedBy = "viajes", fetch = FetchType.LAZY)
    private List<Reservas> reservas;

    // Cambiamos el String plano por la relación ManyToOne
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destino_id")
    private Destino destino;

}

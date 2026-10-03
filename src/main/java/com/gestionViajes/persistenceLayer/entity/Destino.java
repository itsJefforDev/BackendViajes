package com.gestionViajes.persistenceLayer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "destinos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Destino {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion", length = 1000)
    private String descripcion;

    @Column(name = "atracciones_turisticas", length = 1000)
    private String atraccionesTuristicas;

    @Column(name = "clima")
    private String clima;

    @Column(name = "recomendaciones", length = 1000)
    private String recomendaciones;

    @OneToMany(mappedBy = "destino", fetch = FetchType.LAZY)
    private List<Viajes> viajes;
}
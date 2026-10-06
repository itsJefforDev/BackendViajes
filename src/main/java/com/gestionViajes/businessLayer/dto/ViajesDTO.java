package com.gestionViajes.businessLayer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViajesDTO {

    private Long id;
    private Date duracion;
    private double precio;
    private Date fechas_disponibles;
    private String descripcion;

    private List<Long> reservasIds;
    private Long destinoId;
}
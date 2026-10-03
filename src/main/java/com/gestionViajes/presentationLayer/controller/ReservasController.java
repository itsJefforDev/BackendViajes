package com.gestionViajes.presentationLayer.controller;

import com.gestionViajes.businessLayer.dto.ReservasDTO;
import com.gestionViajes.businessLayer.service.ReservasService; // Asegúrate que tu interfaz se llame así o adáptala
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reservas")
@RequiredArgsConstructor // Esto inyecta el servicio automáticamente de forma no estática
public class ReservasController {

    // ¡Ojo! Debe ser una variable de instancia (minúscula al inicio por convención), no estática
    private final ReservasService reservasService;

    @GetMapping
    public ResponseEntity<List<ReservasDTO>> listarTodas() {
        // Llamada correcta desde la instancia 'reservasService'
        List<ReservasDTO> reservas = reservasService.listarTodas();
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservasDTO> obtenerPorId(@PathVariable Long id) {
        ReservasDTO reserva = reservasService.obtenerPorId(id);
        return ResponseEntity.ok(reserva);
    }

    @PostMapping
    public ResponseEntity<ReservasDTO> crear(@RequestBody ReservasDTO reservasDTO) {
        ReservasDTO nuevaReserva = reservasService.crear(reservasDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaReserva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservasDTO> actualizar(@PathVariable Long id, @RequestBody ReservasDTO reservasDTO) {
        ReservasDTO reservaActualizada = reservasService.actualizar(id, reservasDTO);
        return ResponseEntity.ok(reservaActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        reservasService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
package com.gestionViajes.presentationLayer.controller;

import com.gestionViajes.businessLayer.dto.ViajesDTO;
import com.gestionViajes.businessLayer.service.ViajesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/viajes")
@RequiredArgsConstructor
public class ViajesController {

    private final ViajesService viajesService;

    @GetMapping
    public ResponseEntity<List<ViajesDTO>> listarTodos() {
        return ResponseEntity.ok(viajesService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ViajesDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(viajesService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<ViajesDTO> crear(@RequestBody ViajesDTO viajesDTO) {
        ViajesDTO nuevoViaje = viajesService.crear(viajesDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoViaje);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ViajesDTO> actualizar(@PathVariable Long id, @RequestBody ViajesDTO viajesDTO) {
        return ResponseEntity.ok(viajesService.actualizar(id, viajesDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        viajesService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
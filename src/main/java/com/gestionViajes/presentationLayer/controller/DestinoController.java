package com.gestionViajes.presentationLayer.controller;

import com.gestionViajes.businessLayer.dto.DestinoDTO;
import com.gestionViajes.businessLayer.service.DestinoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/destinos")
@RequiredArgsConstructor
public class DestinoController {

    private final DestinoService destinoService;

    @GetMapping
    public ResponseEntity<List<DestinoDTO>> listarTodos() {
        return ResponseEntity.ok(destinoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DestinoDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(destinoService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<DestinoDTO> crear(@RequestBody DestinoDTO destinoDTO) {
        DestinoDTO nuevoDestino = destinoService.crear(destinoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoDestino);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DestinoDTO> actualizar(@PathVariable Long id, @RequestBody DestinoDTO destinoDTO) {
        return ResponseEntity.ok(destinoService.actualizar(id, destinoDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        destinoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
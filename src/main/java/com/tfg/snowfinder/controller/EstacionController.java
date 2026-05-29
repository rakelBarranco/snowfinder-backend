package com.tfg.snowfinder.controller;

import com.tfg.snowfinder.model.Estacion;
import com.tfg.snowfinder.service.EstacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/estaciones")
@CrossOrigin(origins = "http://localhost:4200")
public class EstacionController {

    private final EstacionService estacionService;

    public EstacionController(EstacionService estacionService) {
        this.estacionService = estacionService;
    }

    @GetMapping
    public List<Estacion> getAllEstaciones() {
        return estacionService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEstacionById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(estacionService.getById(id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createEstacion(@RequestBody Estacion estacion) {
        if (estacion.getNombre() == null || estacion.getNombre().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "El nombre es obligatorio"));
        }
        if (estacion.getPais() == null || estacion.getPais().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "El país es obligatorio"));
        }
        if (estacion.getLatitud() == null || estacion.getLongitud() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "La latitud y longitud son obligatorias"));
        }
        return ResponseEntity.ok(estacionService.create(estacion));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEstacion(@PathVariable Integer id, @RequestBody Estacion estacion) {
        try {
            return ResponseEntity.ok(estacionService.update(id, estacion));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEstacion(@PathVariable Integer id) {
        try {
            estacionService.delete(id);
            return ResponseEntity.ok(Map.of("mensaje", "Estación eliminada"));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
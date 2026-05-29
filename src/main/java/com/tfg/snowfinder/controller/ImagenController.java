package com.tfg.snowfinder.controller;

import com.tfg.snowfinder.model.Estacion;
import com.tfg.snowfinder.model.Imagen;
import com.tfg.snowfinder.repository.EstacionRepository;
import com.tfg.snowfinder.repository.ImagenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/imagenes")
@CrossOrigin(origins = "http://localhost:4200")
public class ImagenController {

    @Autowired
    private ImagenRepository imagenRepository;

    @Autowired
    private EstacionRepository estacionRepository;

    @GetMapping("/estacion/{estacionId}")
    public List<Imagen> getImagenesByEstacion(@PathVariable Integer estacionId) {
        return imagenRepository.findByEstacionId(estacionId);
    }

    @PostMapping("/estacion/{estacionId}")
    public ResponseEntity<?> addImagen(@PathVariable Integer estacionId,
                                       @RequestBody Map<String, String> body) {
        Estacion estacion = estacionRepository.findById(estacionId).orElseThrow();
        Imagen imagen = new Imagen();
        imagen.setUrl(body.get("url"));
        imagen.setEstacion(estacion);
        imagenRepository.save(imagen);
        return ResponseEntity.ok(Map.of("mensaje", "Imagen añadida correctamente"));
    }

    @DeleteMapping("/{imagenId}")
    public ResponseEntity<?> deleteImagen(@PathVariable Integer imagenId) {
        imagenRepository.deleteById(imagenId);
        return ResponseEntity.ok(Map.of("mensaje", "Imagen eliminada"));
    }
}
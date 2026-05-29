package com.tfg.snowfinder.controller;

import com.tfg.snowfinder.model.Estacion;
import com.tfg.snowfinder.model.Favorito;
import com.tfg.snowfinder.model.Usuario;
import com.tfg.snowfinder.repository.EstacionRepository;
import com.tfg.snowfinder.repository.FavoritoRepository;
import com.tfg.snowfinder.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/favoritos")
@CrossOrigin(origins = "http://localhost:4200")
public class FavoritoController {

    @Autowired
    private FavoritoRepository favoritoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private EstacionRepository estacionRepository;

    @GetMapping
    public List<Estacion> getMisFavoritos(Principal principal) {
        return favoritoRepository.findByUsuarioEmail(principal.getName())
                .stream()
                .map(Favorito::getEstacion)
                .toList();
    }

    @PostMapping("/{estacionId}")
    public ResponseEntity<?> addFavorito(@PathVariable Integer estacionId, Principal principal) {
        Optional<Favorito> existing = favoritoRepository
                .findByUsuarioEmailAndEstacionId(principal.getName(), estacionId);

        if (existing.isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Ya es favorito"));
        }

        Usuario usuario = usuarioRepository.findByEmail(principal.getName()).orElseThrow();
        Estacion estacion = estacionRepository.findById(estacionId).orElseThrow();

        Favorito favorito = new Favorito();
        favorito.setUsuario(usuario);
        favorito.setEstacion(estacion);
        favoritoRepository.save(favorito);

        return ResponseEntity.ok(Map.of("mensaje", "Añadido a favoritos"));
    }

    @DeleteMapping("/{estacionId}")
    public ResponseEntity<?> removeFavorito(@PathVariable Integer estacionId, Principal principal) {
        Optional<Favorito> favorito = favoritoRepository
                .findByUsuarioEmailAndEstacionId(principal.getName(), estacionId);

        if (favorito.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "No es favorito"));
        }

        favoritoRepository.delete(favorito.get());
        return ResponseEntity.ok(Map.of("mensaje", "Eliminado de favoritos"));
    }
}
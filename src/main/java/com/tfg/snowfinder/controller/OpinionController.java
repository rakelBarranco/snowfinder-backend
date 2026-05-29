package com.tfg.snowfinder.controller;

import com.tfg.snowfinder.dto.OpinionRequest;
import com.tfg.snowfinder.model.Estacion;
import com.tfg.snowfinder.model.Opinion;
import com.tfg.snowfinder.model.Usuario;
import com.tfg.snowfinder.repository.EstacionRepository;
import com.tfg.snowfinder.repository.OpinionRepository;
import com.tfg.snowfinder.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/opiniones")
@CrossOrigin(origins = "http://localhost:4200")
public class OpinionController {

    @Autowired
    private OpinionRepository opinionRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private EstacionRepository estacionRepository;

    @GetMapping("/estacion/{estacionId}")
    public List<Opinion> getOpinionesByEstacion(@PathVariable Integer estacionId) {
        return opinionRepository.findByEstacionId(estacionId);
    }

    @PostMapping("/estacion/{estacionId}")
    public ResponseEntity<?> addOpinion(@PathVariable Integer estacionId,
                                        @RequestBody OpinionRequest request,
                                        Principal principal) {
        Usuario usuario = usuarioRepository.findByEmail(principal.getName()).orElseThrow();
        Estacion estacion = estacionRepository.findById(estacionId).orElseThrow();

        Opinion opinion = new Opinion();
        opinion.setUsuario(usuario);
        opinion.setEstacion(estacion);
        opinion.setPuntuacion(request.getPuntuacion());
        opinion.setComentario(request.getComentario());
        opinionRepository.save(opinion);

        return ResponseEntity.ok(Map.of("mensaje", "Opinión añadida correctamente"));
    }

    @DeleteMapping("/{opinionId}")
    public ResponseEntity<?> deleteOpinion(@PathVariable Integer opinionId) {
        opinionRepository.deleteById(opinionId);
        return ResponseEntity.ok(Map.of("mensaje", "Opinión eliminada"));
    }

    @GetMapping("/mis-opiniones")
    public List<Opinion> getMisOpiniones(Principal principal) {
        return opinionRepository.findByUsuarioEmail(principal.getName());
    }

    @GetMapping("/todas")
    public List<Opinion> getTodasOpiniones() {
        return opinionRepository.findAll();
    }
}

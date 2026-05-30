package com.tfg.snowfinder.controller;

import com.tfg.snowfinder.model.Estacion;
import com.tfg.snowfinder.model.Reserva;
import com.tfg.snowfinder.model.Usuario;
import com.tfg.snowfinder.repository.EstacionRepository;
import com.tfg.snowfinder.repository.ReservaRepository;
import com.tfg.snowfinder.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "http://localhost:4200")
public class ReservaController {

    @Autowired
    private ReservaRepository reservaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private EstacionRepository estacionRepository;

    @GetMapping("/mis-reservas")
    public List<Reserva> getMisReservas(Principal principal) {
        return reservaRepository.findByUsuarioEmail(principal.getName());
    }

    @PostMapping("/estacion/{estacionId}")
    public ResponseEntity<?> crearReserva(@PathVariable Integer estacionId,
                                          @RequestBody Map<String, String> body,
                                          Principal principal) {
        Usuario usuario = usuarioRepository.findByEmail(principal.getName()).orElseThrow();
        Estacion estacion = estacionRepository.findById(estacionId).orElseThrow();

        Reserva reserva = new Reserva();
        reserva.setUsuario(usuario);
        reserva.setEstacion(estacion);
        reserva.setFecha(LocalDate.parse(body.get("fecha")));
        reservaRepository.save(reserva);

        return ResponseEntity.ok(Map.of("mensaje", "Reserva creada correctamente"));
    }

    @DeleteMapping("/{reservaId}")
    public ResponseEntity<?> cancelarReserva(@PathVariable Integer reservaId, Principal principal) {
        Reserva reserva = reservaRepository.findById(reservaId).orElseThrow();
        reservaRepository.delete(reserva);
        return ResponseEntity.ok(Map.of("mensaje", "Reserva cancelada correctamente"));
    }
}
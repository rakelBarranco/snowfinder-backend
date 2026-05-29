package com.tfg.snowfinder.controller;

import com.tfg.snowfinder.dto.LoginRequest;
import com.tfg.snowfinder.dto.RegisterRequest;
import com.tfg.snowfinder.model.Usuario;
import com.tfg.snowfinder.repository.UsuarioRepository;
import com.tfg.snowfinder.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email ya registrado"));
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setRol("USER");

        usuarioRepository.save(usuario);
        return ResponseEntity.ok(Map.of("mensaje", "Usuario registrado correctamente"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(request.getEmail());

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Usuario no encontrado"));
        }

        Usuario usuario = usuarioOpt.get();

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Contraseña incorrecta"));
        }

        String token = jwtUtil.generateToken(usuario.getEmail(), usuario.getRol());
        return ResponseEntity.ok(Map.of("token", token, "rol", usuario.getRol(), "nombre", usuario.getNombre()));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMe(Principal principal) {
        Usuario usuario = usuarioRepository.findByEmail(principal.getName()).orElseThrow();
        return ResponseEntity.ok(Map.of(
                "nombre", usuario.getNombre(),
                "email", usuario.getEmail(),
                "rol", usuario.getRol(),
                "fotoPerfil", usuario.getFotoPerfil() != null ? usuario.getFotoPerfil() : ""
        ));
    }

    @PutMapping("/foto-perfil")
    public ResponseEntity<?> actualizarFotoPerfil(@RequestBody Map<String, String> body, Principal principal) {
        Usuario usuario = usuarioRepository.findByEmail(principal.getName()).orElseThrow();
        usuario.setFotoPerfil(body.get("url"));
        usuarioRepository.save(usuario);
        return ResponseEntity.ok(Map.of("mensaje", "Foto de perfil actualizada"));
    }

    @PutMapping("/nombre")
    public ResponseEntity<?> actualizarNombre(@RequestBody Map<String, String> body, Principal principal) {
        Usuario usuario = usuarioRepository.findByEmail(principal.getName()).orElseThrow();
        usuario.setNombre(body.get("nombre"));
        usuarioRepository.save(usuario);
        return ResponseEntity.ok(Map.of("mensaje", "Nombre actualizado correctamente"));
    }

    @PutMapping("/password")
    public ResponseEntity<?> cambiarPassword(@RequestBody Map<String, String> body, Principal principal) {
        Usuario usuario = usuarioRepository.findByEmail(principal.getName()).orElseThrow();
        if (!passwordEncoder.matches(body.get("passwordActual"), usuario.getPassword())) {
            return ResponseEntity.badRequest().body(Map.of("error", "La contraseña actual no es correcta"));
        }
        usuario.setPassword(passwordEncoder.encode(body.get("passwordNueva")));
        usuarioRepository.save(usuario);
        return ResponseEntity.ok(Map.of("mensaje", "Contraseña actualizada correctamente"));
    }

    @DeleteMapping("/cuenta")
    public ResponseEntity<?> eliminarCuenta(Principal principal) {
        Usuario usuario = usuarioRepository.findByEmail(principal.getName()).orElseThrow();
        usuarioRepository.delete(usuario);
        return ResponseEntity.ok(Map.of("mensaje", "Cuenta eliminada correctamente"));
    }
}
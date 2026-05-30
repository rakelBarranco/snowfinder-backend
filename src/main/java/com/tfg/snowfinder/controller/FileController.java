package com.tfg.snowfinder.controller;

import com.tfg.snowfinder.model.Estacion;
import com.tfg.snowfinder.model.Imagen;
import com.tfg.snowfinder.model.Usuario;
import com.tfg.snowfinder.repository.EstacionRepository;
import com.tfg.snowfinder.repository.ImagenRepository;
import com.tfg.snowfinder.repository.UsuarioRepository;
import com.tfg.snowfinder.service.CloudinaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "http://localhost:4200")
public class FileController {

    private final ImagenRepository imagenRepository;
    private final EstacionRepository estacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final CloudinaryService cloudinaryService;

    public FileController(ImagenRepository imagenRepository,
                          EstacionRepository estacionRepository,
                          UsuarioRepository usuarioRepository,
                          CloudinaryService cloudinaryService) {
        this.imagenRepository = imagenRepository;
        this.estacionRepository = estacionRepository;
        this.usuarioRepository = usuarioRepository;
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping("/upload/{estacionId}")
    public ResponseEntity<?> uploadImagen(@PathVariable Integer estacionId,
                                          @RequestParam("file") MultipartFile file) {
        try {
            String url = cloudinaryService.uploadFile(file);

            Estacion estacion = estacionRepository.findById(estacionId).orElseThrow();
            Imagen imagen = new Imagen();
            imagen.setUrl(url);
            imagen.setEstacion(estacion);
            imagenRepository.save(imagen);

            return ResponseEntity.ok(Map.of("url", url, "mensaje", "Imagen subida correctamente"));

        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error al subir la imagen"));
        }
    }

    @PostMapping("/upload/perfil")
    public ResponseEntity<?> uploadFotoPerfil(@RequestParam("file") MultipartFile file,
                                              Principal principal) {
        try {
            String url = cloudinaryService.uploadFile(file);

            Usuario usuario = usuarioRepository.findByEmail(principal.getName()).orElseThrow();
            usuario.setFotoPerfil(url);
            usuarioRepository.save(usuario);

            return ResponseEntity.ok(Map.of("url", url, "mensaje", "Foto de perfil actualizada"));

        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error al subir la foto de perfil"));
        }
    }
}
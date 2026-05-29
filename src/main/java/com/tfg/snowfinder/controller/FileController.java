package com.tfg.snowfinder.controller;

import com.tfg.snowfinder.model.Estacion;
import com.tfg.snowfinder.model.Imagen;
import com.tfg.snowfinder.model.Usuario;
import com.tfg.snowfinder.repository.EstacionRepository;
import com.tfg.snowfinder.repository.ImagenRepository;
import com.tfg.snowfinder.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "http://localhost:4200")
public class FileController {

    @Value("${app.upload.dir}")
    private String uploadDir;

    private final ImagenRepository imagenRepository;
    private final EstacionRepository estacionRepository;
    private final UsuarioRepository usuarioRepository;

    public FileController(ImagenRepository imagenRepository,
                          EstacionRepository estacionRepository,
                          UsuarioRepository usuarioRepository) {
        this.imagenRepository = imagenRepository;
        this.estacionRepository = estacionRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/upload/{estacionId}")
    public ResponseEntity<?> uploadImagen(@PathVariable Integer estacionId,
                                          @RequestParam("file") MultipartFile file) {
        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String extension = getExtension(file.getOriginalFilename());
            String filename = UUID.randomUUID() + extension;

            Path filePath = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), filePath);

            String url = "http://localhost:8080/uploads/imagenes/" + filename;
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
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String extension = getExtension(file.getOriginalFilename());
            String filename = "perfil_" + UUID.randomUUID() + extension;

            Path filePath = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), filePath);

            String url = "http://localhost:8080/uploads/imagenes/" + filename;

            Usuario usuario = usuarioRepository.findByEmail(principal.getName()).orElseThrow();
            usuario.setFotoPerfil(url);
            usuarioRepository.save(usuario);

            return ResponseEntity.ok(Map.of("url", url, "mensaje", "Foto de perfil actualizada"));

        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error al subir la foto de perfil"));
        }
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) return ".jpg";
        return filename.substring(filename.lastIndexOf("."));
    }
}
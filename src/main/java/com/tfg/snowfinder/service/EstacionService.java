package com.tfg.snowfinder.service;

import com.tfg.snowfinder.model.Estacion;
import com.tfg.snowfinder.repository.EstacionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EstacionService {

    private final EstacionRepository estacionRepository;

    public EstacionService(EstacionRepository estacionRepository) {
        this.estacionRepository = estacionRepository;
    }

    public List<Estacion> getAll() {
        return estacionRepository.findAll();
    }

    public Estacion getById(Integer id) {
        return estacionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Estación no encontrada con id: " + id));
    }

    public Estacion create(Estacion estacion) {
        return estacionRepository.save(estacion);
    }

    public Estacion update(Integer id, Estacion estacion) {
        if (!estacionRepository.existsById(id)) {
            throw new NoSuchElementException("Estación no encontrada con id: " + id);
        }
        estacion.setId(id);
        if (estacion.getImagenes() != null) {
            estacion.getImagenes().forEach(img -> img.setEstacion(estacion));
        }
        return estacionRepository.save(estacion);
    }

    public void delete(Integer id) {
        if (!estacionRepository.existsById(id)) {
            throw new NoSuchElementException("Estación no encontrada con id: " + id);
        }
        estacionRepository.deleteById(id);
    }
}
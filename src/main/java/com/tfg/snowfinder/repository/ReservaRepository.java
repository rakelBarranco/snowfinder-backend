package com.tfg.snowfinder.repository;

import com.tfg.snowfinder.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Integer> {
    List<Reserva> findByUsuarioEmail(String email);
}
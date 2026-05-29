package com.tfg.snowfinder.repository;

import com.tfg.snowfinder.model.Favorito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FavoritoRepository extends JpaRepository<Favorito, Integer> {
    List<Favorito> findByUsuarioEmail(String email);
    Optional<Favorito> findByUsuarioEmailAndEstacionId(String email, Integer estacionId);
}
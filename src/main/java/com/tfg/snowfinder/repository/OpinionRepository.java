package com.tfg.snowfinder.repository;

import com.tfg.snowfinder.model.Opinion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OpinionRepository extends JpaRepository<Opinion, Integer> {
    List<Opinion> findByEstacionId(Integer estacionId);
    List<Opinion> findByUsuarioEmail(String email);
}
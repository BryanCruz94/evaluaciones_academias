package com.nairbdev.academiasbackend.repository;

import com.nairbdev.academiasbackend.entity.PruebaFisica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PruebaFisicaRepository extends JpaRepository<PruebaFisica, Long> {

    List<PruebaFisica> findByAcademiaId(Long academiaId);

}

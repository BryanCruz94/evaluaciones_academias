package com.nairbdev.academiasbackend.repository;

import com.nairbdev.academiasbackend.entity.PruebaFisica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PruebaFisicaRepository extends JpaRepository<PruebaFisica, Long> {

    // Listar catálogo de pruebas físicas activas por academia
    List<PruebaFisica> findByAcademiaIdAndActivoTrueOrderByNombreAsc(Long academiaId);

    // Buscar una prueba física del catálogo validando academia
    Optional<PruebaFisica> findByIdAndAcademiaIdAndActivoTrue(Long id, Long academiaId);

    // Evitar duplicado por nombre (ya lo tienes también por constraint en BD)
    boolean existsByAcademiaIdAndNombreIgnoreCase(Long academiaId, String nombre);
}
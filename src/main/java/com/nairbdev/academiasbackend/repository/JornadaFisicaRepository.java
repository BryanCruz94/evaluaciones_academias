package com.nairbdev.academiasbackend.repository;

import com.nairbdev.academiasbackend.entity.JornadaFisica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface JornadaFisicaRepository extends JpaRepository<JornadaFisica, Long> {

    List<JornadaFisica> findByAlumnoIdOrderByFechaDesc(Long alumnoId);

    Optional<JornadaFisica> findByAlumnoIdAndFecha(Long alumnoId, LocalDate fecha);

}

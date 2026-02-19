package com.nairbdev.academiasbackend.repository;

import com.nairbdev.academiasbackend.entity.JornadaAcademica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JornadaAcademicaRepository extends JpaRepository<JornadaAcademica, Long> {

    List<JornadaAcademica> findByAlumnoIdOrderByFechaDesc(Long alumnoId);

}

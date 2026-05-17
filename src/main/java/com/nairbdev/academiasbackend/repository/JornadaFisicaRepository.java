package com.nairbdev.academiasbackend.repository;

import com.nairbdev.academiasbackend.entity.JornadaFisica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface JornadaFisicaRepository extends JpaRepository<JornadaFisica, Long> {

    List<JornadaFisica> findByAlumnoIdOrderByFechaDesc(Long alumnoId);

    Optional<JornadaFisica> findByAlumnoIdAndFecha(Long alumnoId, LocalDate fecha);

    @Query("""
            select distinct j from JornadaFisica j
            join fetch j.alumno a
            join fetch a.academia
            left join fetch a.programaActual
            join fetch j.programa p
            where j.academia.id = :academiaId
            order by j.fecha asc
            """)
    List<JornadaFisica> findConsolidadoByAcademiaId(@Param("academiaId") Long academiaId);

}

package com.nairbdev.academiasbackend.repository;

import com.nairbdev.academiasbackend.entity.Alumno;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AlumnoRepository extends JpaRepository<Alumno, Long> {

    @Query("select a from Alumno a join fetch a.academia")
    List<Alumno> findAllConAcademia();

    @Query("select a from Alumno a join fetch a.academia where a.academia.id = :academiaId and a.estado = 'ACTIVO'")
    List<Alumno> findActivosPorAcademia(@Param("academiaId") Long academiaId);

    @Query("select a from Alumno a join fetch a.academia where a.cedula = :cedula")
    Optional<Alumno> findByCedulaConAcademia(@Param("cedula") String cedula);
}


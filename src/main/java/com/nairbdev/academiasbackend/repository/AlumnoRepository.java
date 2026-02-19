package com.nairbdev.academiasbackend.repository;

import com.nairbdev.academiasbackend.entity.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlumnoRepository extends JpaRepository<Alumno, Long> {

    List<Alumno> findByAcademiaId(Long academiaId);

}

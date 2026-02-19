package com.nairbdev.academiasbackend.repository;

import com.nairbdev.academiasbackend.entity.Materia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MateriaRepository extends JpaRepository<Materia, Long> {

    List<Materia> findByAcademiaId(Long academiaId);

}

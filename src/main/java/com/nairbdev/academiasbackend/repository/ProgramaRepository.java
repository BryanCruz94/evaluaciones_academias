package com.nairbdev.academiasbackend.repository;

import com.nairbdev.academiasbackend.entity.Programa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgramaRepository extends JpaRepository<Programa, Long> {

    List<Programa> findByAcademiaIdAndActivoTrue(Long academiaId);

}

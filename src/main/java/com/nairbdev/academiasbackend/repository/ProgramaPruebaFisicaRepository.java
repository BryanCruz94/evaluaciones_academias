package com.nairbdev.academiasbackend.repository;

import com.nairbdev.academiasbackend.entity.Genero;
import com.nairbdev.academiasbackend.entity.ProgramaPruebaFisica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProgramaPruebaFisicaRepository extends JpaRepository<ProgramaPruebaFisica, Long> {

    List<ProgramaPruebaFisica> findByProgramaIdAndActivoTrueOrderByIdAsc(Long programaId);

    Optional<ProgramaPruebaFisica> findByIdAndProgramaId(Long id, Long programaId);
}
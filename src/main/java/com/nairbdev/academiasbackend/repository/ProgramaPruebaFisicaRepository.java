package com.nairbdev.academiasbackend.repository;

import com.nairbdev.academiasbackend.entity.ProgramaPruebaFisica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgramaPruebaFisicaRepository extends JpaRepository<ProgramaPruebaFisica, Long> {

    List<ProgramaPruebaFisica> findByProgramaIdOrderByOrdenAsc(Long programaId);

}

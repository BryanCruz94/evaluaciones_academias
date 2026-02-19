package com.nairbdev.academiasbackend.repository;

import com.nairbdev.academiasbackend.entity.ResultadoFisico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResultadoFisicoRepository extends JpaRepository<ResultadoFisico, Long> {

    List<ResultadoFisico> findByJornadaFisicaId(Long jornadaFisicaId);

}

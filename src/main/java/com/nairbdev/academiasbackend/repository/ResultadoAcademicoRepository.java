package com.nairbdev.academiasbackend.repository;

import com.nairbdev.academiasbackend.entity.ResultadoAcademico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResultadoAcademicoRepository extends JpaRepository<ResultadoAcademico, Long> {

    List<ResultadoAcademico> findByJornadaAcademicaId(Long jornadaAcademicaId);

}

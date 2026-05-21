package com.nairbdev.academiasbackend.repository;

import com.nairbdev.academiasbackend.entity.ConsultaAlumnoAuditoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsultaAlumnoAuditoriaRepository extends JpaRepository<ConsultaAlumnoAuditoria, Long> {
    List<ConsultaAlumnoAuditoria> findAllByOrderByFechaConsultaDesc();
}

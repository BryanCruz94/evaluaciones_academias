package com.nairbdev.academiasbackend.service;

import com.nairbdev.academiasbackend.dto.auditoria.ConsultaAlumnoAuditoriaDTO;
import com.nairbdev.academiasbackend.entity.Alumno;
import com.nairbdev.academiasbackend.entity.ConsultaAlumnoAuditoria;
import com.nairbdev.academiasbackend.repository.ConsultaAlumnoAuditoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class ConsultaAlumnoAuditoriaService {

    private final ConsultaAlumnoAuditoriaRepository repository;

    public ConsultaAlumnoAuditoriaService(ConsultaAlumnoAuditoriaRepository repository) {
        this.repository = repository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registrarConsulta(String nombreConsultor, String cedulaConsultada, Alumno alumnoEncontrado) {
        ConsultaAlumnoAuditoria auditoria = new ConsultaAlumnoAuditoria();
        auditoria.setFechaConsulta(OffsetDateTime.now());
        auditoria.setNombreConsultor(normalizarNombreConsultor(nombreConsultor));
        auditoria.setCedulaConsultada(cedulaConsultada);

        if (alumnoEncontrado != null) {
            auditoria.setAlumnoId(alumnoEncontrado.getId());
            auditoria.setNombreAlumno(
                    (alumnoEncontrado.getApellidos() + " " + alumnoEncontrado.getNombres()).trim()
            );
            auditoria.setResultadoEncontrado(true);
        } else {
            auditoria.setResultadoEncontrado(false);
        }

        repository.save(auditoria);
    }

    @Transactional(readOnly = true)
    public List<ConsultaAlumnoAuditoriaDTO> listarConsultas() {
        return repository.findAllByOrderByFechaConsultaDesc()
                .stream()
                .map(this::toDto)
                .toList();
    }

    private String normalizarNombreConsultor(String nombreConsultor) {
        if (nombreConsultor == null || nombreConsultor.isBlank()) {
            return "Usuario autenticado";
        }

        return nombreConsultor.trim();
    }

    private ConsultaAlumnoAuditoriaDTO toDto(ConsultaAlumnoAuditoria auditoria) {
        return new ConsultaAlumnoAuditoriaDTO(
                auditoria.getId(),
                auditoria.getFechaConsulta(),
                auditoria.getNombreConsultor(),
                auditoria.getCedulaConsultada(),
                auditoria.getAlumnoId(),
                auditoria.getNombreAlumno(),
                auditoria.getResultadoEncontrado()
        );
    }
}

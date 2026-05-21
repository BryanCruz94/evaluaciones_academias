package com.nairbdev.academiasbackend.dto.auditoria;

import java.time.OffsetDateTime;

public record ConsultaAlumnoAuditoriaDTO(
        Long id,
        OffsetDateTime fechaConsulta,
        String nombreConsultor,
        String cedulaConsultada,
        Long alumnoId,
        String nombreAlumno,
        Boolean resultadoEncontrado
) {
}

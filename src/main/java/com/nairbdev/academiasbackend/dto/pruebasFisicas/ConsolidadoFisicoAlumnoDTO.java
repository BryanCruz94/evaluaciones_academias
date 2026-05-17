package com.nairbdev.academiasbackend.dto.pruebasFisicas;

import java.util.List;

public record ConsolidadoFisicoAlumnoDTO(
        Long alumnoId,
        String apellidos,
        String nombres,
        String nombreCompleto,
        List<ConsolidadoFisicoCeldaDTO> evaluaciones,
        String programa
) {
}

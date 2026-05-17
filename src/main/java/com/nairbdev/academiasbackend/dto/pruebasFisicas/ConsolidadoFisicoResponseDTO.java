package com.nairbdev.academiasbackend.dto.pruebasFisicas;

import java.time.LocalDate;
import java.util.List;

public record ConsolidadoFisicoResponseDTO(
        List<LocalDate> fechas,
        List<ConsolidadoFisicoAlumnoDTO> alumnos
) {
}

package com.nairbdev.academiasbackend.dto.pruebasFisicas;

import java.time.LocalDate;
import java.util.List;

public record PruebasFisicasTablaAlumnoDTO(
        Long alumnoId,
        List<Long> baterias,
        List<LocalDate> fechas,
        String ultimaPruebaEvaluada,
        List<PruebaFisicaFilaDTO> filas,
        List<PruebaFisicaBateriaResumenDTO> resumenFinal
) {
}

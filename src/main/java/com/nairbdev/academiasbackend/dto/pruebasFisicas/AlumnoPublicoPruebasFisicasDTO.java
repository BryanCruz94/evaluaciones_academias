package com.nairbdev.academiasbackend.dto.pruebasFisicas;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AlumnoPublicoPruebasFisicasDTO(
        Long id,
        String nombres,
        String apellidos,
        String cedula,
        String genero,
        LocalDate fechaNacimiento,
        Double estaturaCm,
        Double pesoKg,
        BigDecimal imc,
        String fotoUrl,
        String programaActual,
        PruebasFisicasTablaAlumnoDTO pruebasFisicas
) {
}

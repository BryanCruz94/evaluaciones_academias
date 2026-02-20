package com.nairbdev.academiasbackend.dto.alumnos;

import com.nairbdev.academiasbackend.entity.Genero;

import java.time.LocalDate;

public record AlumnoUpdateDto(
        String apellidos,
        String nombres,
        String email,
        LocalDate fechaNacimiento,
        Genero genero,
        LocalDate fechaIngreso,
        LocalDate fechaSalida,
        String fotoUrl,
        Double estaturaCm,
        Double pesoKg,
        Long programaActualId,
        String cedula
) {}

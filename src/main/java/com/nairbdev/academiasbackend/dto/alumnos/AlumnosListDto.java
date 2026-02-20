package com.nairbdev.academiasbackend.dto.alumnos;

import com.nairbdev.academiasbackend.entity.EstadoAlumno;

import java.time.LocalDate;

public record AlumnosListDto(
        Long id,
        String academiaNombre,
        String apellidos,
        String nombres,
        String cedula,
        String email,
        String genero,          // "Masculino" / "Femenino"
        LocalDate fechaIngreso,
        LocalDate fechaSalida,
        String programaActual,
        EstadoAlumno estado
) {}
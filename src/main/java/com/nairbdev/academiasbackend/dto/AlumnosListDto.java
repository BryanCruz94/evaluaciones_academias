package com.nairbdev.academiasbackend.dto;

import com.nairbdev.academiasbackend.entity.EstadoAlumno;

import java.time.LocalDate;

public record AlumnosListDto(
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
package com.nairbdev.academiasbackend.dto.alumnos;

import com.nairbdev.academiasbackend.entity.Genero;
import java.time.LocalDate;

public record AlumnoCreateDto(
        String apellidos,
        String nombres,
        String cedula,
        String email,
        LocalDate fechaNacimiento,
        Genero genero,
        LocalDate fechaIngreso,
        LocalDate fechaSalida,
        Double estaturaCm,
        Double pesoKg,
        Long programaActualId
) {}

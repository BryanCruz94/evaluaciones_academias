package com.nairbdev.academiasbackend.dto;

import com.nairbdev.academiasbackend.entity.EstadoAlumno;

import java.time.LocalDate;

public record AlumnoInfoDto(
        String academiaNombre,
        String apellidos,
        String nombres,
        String cedula,
        String email,
        LocalDate fechaNacimiento,
        Integer edadActual,
        String genero,          // "Masculino" / "Femenino"
        LocalDate fechaIngreso,
        LocalDate fechaSalida,
        String urlFoto,
        Double estatura,
        Double peso,
        Double imc,
        String programaActual,
        EstadoAlumno estado
) {}
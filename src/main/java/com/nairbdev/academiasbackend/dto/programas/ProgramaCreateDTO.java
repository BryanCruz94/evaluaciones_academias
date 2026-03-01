package com.nairbdev.academiasbackend.dto.programas;

public record ProgramaCreateDTO(
        String nombre,
        String descripcion,
        String genero,
        Boolean activo
) {}
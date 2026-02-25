package com.nairbdev.academiasbackend.dto.programas;

public record ProgramaCreateDTO(
        String nombre,
        String descripcion,
        Boolean activo
) {}
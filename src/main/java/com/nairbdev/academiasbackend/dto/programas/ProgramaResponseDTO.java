package com.nairbdev.academiasbackend.dto.programas;

public record ProgramaResponseDTO(
        Long id,
        String academiaNombre,
        String nombre,
        String descripcion,
        Boolean activo
) {}
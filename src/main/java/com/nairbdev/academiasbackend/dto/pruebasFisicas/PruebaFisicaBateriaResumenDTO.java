package com.nairbdev.academiasbackend.dto.pruebasFisicas;

public record PruebaFisicaBateriaResumenDTO(
        Long bateriaId,
        String signo,
        Boolean aprobado,
        Double diferencia
) {
}

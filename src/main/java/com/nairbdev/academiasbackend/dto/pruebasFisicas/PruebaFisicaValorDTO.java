package com.nairbdev.academiasbackend.dto.pruebasFisicas;

public record PruebaFisicaValorDTO(
        Long bateriaId,
        Double valorObjetivo,
        Double valorMarcado,
        Boolean valorBooleano,
        String valorTexto
) {
}

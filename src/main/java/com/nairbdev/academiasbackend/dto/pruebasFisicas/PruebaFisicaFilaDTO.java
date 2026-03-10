package com.nairbdev.academiasbackend.dto.pruebasFisicas;

import java.util.List;

public record PruebaFisicaFilaDTO(
        String nombre,
        String unidad,
        List<PruebaFisicaValorDTO> valores
) {
}

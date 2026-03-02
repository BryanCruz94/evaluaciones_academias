package com.nairbdev.academiasbackend.dto.jornadaFisica;

import java.math.BigDecimal;

public record ResultadoFisicoCreateItem(
        Long programaPruebaFisicaId,
        BigDecimal valorNum,    // REPETICIONES, NUMERICO, TIEMPO (en segundos)
        Boolean valorBool,      // BOOLEANO (SI/NO)
        String observacion
) { }
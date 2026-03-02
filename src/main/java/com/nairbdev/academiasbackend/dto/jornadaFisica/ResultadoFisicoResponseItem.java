package com.nairbdev.academiasbackend.dto.jornadaFisica;

import com.nairbdev.academiasbackend.entity.TipoValorPrueba;

import java.math.BigDecimal;

public record ResultadoFisicoResponseItem(
        Long id,
        Long programaPruebaFisicaId,
        String nombrePrueba,
        TipoValorPrueba tipoValor,
        String unidad,
        BigDecimal valorNum,
        String valorTiempoFormateado, // solo si TIEMPO
        Boolean valorBool,
        String observacion
) { }
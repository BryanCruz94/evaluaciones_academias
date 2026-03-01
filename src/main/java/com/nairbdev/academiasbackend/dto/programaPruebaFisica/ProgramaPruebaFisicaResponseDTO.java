package com.nairbdev.academiasbackend.dto.programaPruebaFisica;

import com.nairbdev.academiasbackend.entity.OperadorObjetivo;
import com.nairbdev.academiasbackend.entity.TipoValorPrueba;

import java.math.BigDecimal;

public record ProgramaPruebaFisicaResponseDTO(
        Long id,
        Long programaId,
        String nombre,
        String descripcion,
        TipoValorPrueba tipoValor,
        String unidad,

        // valor crudo en BD (segundos o 0/1 o número)
        BigDecimal objetivoValor,

        // ✅ valores formateados para frontend
        String objetivoTiempo,
        Boolean objetivoBooleano,

        OperadorObjetivo operador,
        String etiqueta,
        Boolean activo
) {}
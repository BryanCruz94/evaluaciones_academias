package com.nairbdev.academiasbackend.dto.programaPruebaFisica;

import com.nairbdev.academiasbackend.entity.OperadorObjetivo;
import com.nairbdev.academiasbackend.entity.TipoValorPrueba;

import java.math.BigDecimal;

public record ProgramaPruebaFisicaItemDTO(
        Long id,                 // null = nueva fila
        String nombre,
        String descripcion,
        TipoValorPrueba tipoValor,
        String unidad,

        // ✅ para NUMERICO / REPETICIONES y también aquí guardaremos segundos (TIEMPO) o 0/1 (BOOLEANO)
        BigDecimal objetivoValor,

        // ✅ solo cuando tipoValor == TIEMPO (mm:ss o hh:mm:ss)
        String objetivoTiempo,

        // ✅ solo cuando tipoValor == BOOLEANO (SI/NO)
        Boolean objetivoBooleano,

        OperadorObjetivo operador,
        String etiqueta,
        Boolean activo            // opcional, si no envías lo manejo yo
) {}
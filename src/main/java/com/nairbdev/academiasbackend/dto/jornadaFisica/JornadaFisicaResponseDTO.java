package com.nairbdev.academiasbackend.dto.jornadaFisica;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record JornadaFisicaResponseDTO(
        Long id,
        Long alumnoId,
        Long programaId,
        LocalDate fecha,
        Long evaluadorId,
        String observacion,
        Double pesoKg,
        BigDecimal imc,
        List<ResultadoFisicoResponseItem> resultados
) { }
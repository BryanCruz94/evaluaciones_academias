package com.nairbdev.academiasbackend.dto.jornadaFisica;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record JornadaFisicaCreateRequest(
        LocalDate fechaEvaluacion,
        Double pesoKg,
        Double estaturaCm,      // viene del front (puedes actualizar alumno)
        String observacion,
        List<ResultadoFisicoCreateItem> resultados
) { }
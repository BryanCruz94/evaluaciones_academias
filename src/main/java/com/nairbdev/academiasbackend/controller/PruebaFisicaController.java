package com.nairbdev.academiasbackend.controller;

import com.nairbdev.academiasbackend.dto.pruebasFisicas.PruebasFisicasTablaAlumnoDTO;
import com.nairbdev.academiasbackend.service.JornadaFisicaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api-registro")
@RequiredArgsConstructor
@Tag(name = "Pruebas Físicas", description = "Consulta de baterías y resultados físicos por alumno")
public class PruebaFisicaController {

    private final JornadaFisicaService jornadaFisicaService;

    @Operation(
            summary = "Obtener todas las baterías físicas del alumno en formato tabla",
            description = "Retorna filas por prueba física y columnas por batería (fecha), con objetivo vs marcado y resumen final de aprobación de la última prueba."
    )
    @GetMapping("/academia/{academiaId}/alumnos/{alumnoId}/baterias-pruebas-fisicas")
    public PruebasFisicasTablaAlumnoDTO obtenerTablaBaterias(
            @PathVariable Long academiaId,
            @PathVariable Long alumnoId
    ) {
        return jornadaFisicaService.obtenerTablaBateriasPorAlumno(academiaId, alumnoId);
    }

}

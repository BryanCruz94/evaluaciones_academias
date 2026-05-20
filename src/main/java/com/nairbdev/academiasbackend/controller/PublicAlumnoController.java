package com.nairbdev.academiasbackend.controller;

import com.nairbdev.academiasbackend.dto.pruebasFisicas.AlumnoPublicoPruebasFisicasDTO;
import com.nairbdev.academiasbackend.service.JornadaFisicaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/alumnos")
@Tag(name = "Consulta publica", description = "Consulta publica de informacion fisica por cedula")
public class PublicAlumnoController {

    private final JornadaFisicaService jornadaFisicaService;

    public PublicAlumnoController(JornadaFisicaService jornadaFisicaService) {
        this.jornadaFisicaService = jornadaFisicaService;
    }

    @Operation(
            summary = "Consultar pruebas fisicas por cedula",
            description = "Endpoint publico de solo lectura para padres de familia. No requiere JWT."
    )
    @GetMapping("/cedula/{cedula}/pruebas-fisicas")
    public AlumnoPublicoPruebasFisicasDTO obtenerPruebasFisicasPorCedula(@PathVariable String cedula) {
        return jornadaFisicaService.obtenerMatrizFisicaPublicaPorCedula(cedula);
    }
}

package com.nairbdev.academiasbackend.controller;

import com.nairbdev.academiasbackend.entity.Academia;
import com.nairbdev.academiasbackend.service.AcademiaService;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api-registro/academias")
@Tag(name = "Academias", description = "Gestión y consulta de academias registradas en el sistema")
public class AcademiaController {

    private final AcademiaService academiaService;

    public AcademiaController(AcademiaService academiaService) {
        this.academiaService = academiaService;
    }

    @Operation(
            summary = "Listar todas las academias",
            description = "Devuelve todas las academias registradas en el sistema (activas e inactivas)"
    )
    @GetMapping
    public List<Academia> listarTodas() {
        return academiaService.listarTodas();
    }

    @Operation(
            summary = "Listar academias activas",
            description = "Devuelve únicamente las academias con estado activo = true"
    )
    @GetMapping("/activas")
    public List<Academia> listarActivas() {
        return academiaService.listarActivas();
    }

    @Operation(
            summary = "Buscar academia por código",
            description = "Busca una academia utilizando su código único institucional"
    )
    @GetMapping("/codigo/{codigo}")
    public Academia buscarPorCodigo(@PathVariable String codigo) {
        return academiaService.buscarPorCodigo(codigo);
    }
}
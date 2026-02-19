package com.nairbdev.academiasbackend.controller;

import com.nairbdev.academiasbackend.entity.Academia;
import com.nairbdev.academiasbackend.service.AcademiaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api-registro/academias")
public class AcademiaController {

    private final AcademiaService academiaService;

    public AcademiaController(AcademiaService academiaService) {
        this.academiaService = academiaService;
    }

    // GET /api/academias
    @GetMapping
    public List<Academia> listarTodas() {
        return academiaService.listarTodas();
    }

    // GET /api/academias/activas
    @GetMapping("/activas")
    public List<Academia> listarActivas() {
        return academiaService.listarActivas();
    }

    // GET /api/academias/codigo/{codigo}
    @GetMapping("/codigo/{codigo}")
    public Academia buscarPorCodigo(@PathVariable String codigo) {
        return academiaService.buscarPorCodigo(codigo);
    }
}

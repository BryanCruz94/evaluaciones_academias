package com.nairbdev.academiasbackend.controller;

import com.nairbdev.academiasbackend.dto.auditoria.ConsultaAlumnoAuditoriaDTO;
import com.nairbdev.academiasbackend.service.ConsultaAlumnoAuditoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api-registro/auditoria-busquedas-alumnos")
@Tag(name = "Auditoria de busquedas", description = "Registros de busquedas de alumnos por cedula")
public class ConsultaAlumnoAuditoriaController {

    private final ConsultaAlumnoAuditoriaService service;

    public ConsultaAlumnoAuditoriaController(ConsultaAlumnoAuditoriaService service) {
        this.service = service;
    }

    @Operation(
            summary = "Listar busquedas de alumnos",
            description = "Endpoint protegido para administradores. Devuelve auditoria de consultas por cedula."
    )
    @GetMapping
    public List<ConsultaAlumnoAuditoriaDTO> listar() {
        return service.listarConsultas();
    }
}

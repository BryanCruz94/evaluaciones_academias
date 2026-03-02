package com.nairbdev.academiasbackend.controller;

import com.nairbdev.academiasbackend.dto.programas.ProgramaCreateDTO;
import com.nairbdev.academiasbackend.dto.programas.ProgramaResponseDTO;
import com.nairbdev.academiasbackend.dto.programas.ProgramaUpdateDTO;
import com.nairbdev.academiasbackend.service.ProgramaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api-registro/programas")
@RequiredArgsConstructor
@Tag(name = "Programas", description = "Gestión de programas de preparación por academia")
public class ProgramaController {

    private final ProgramaService programaService;

    @Operation(
            summary = "Listar todos los programas",
            description = "Devuelve todos los programas registrados en el sistema (activos e inactivos)"
    )
    @GetMapping
    public List<ProgramaResponseDTO> findAll() {
        return programaService.findAll();
    }

    @Operation(
            summary = "Listar programas activos por academia",
            description = "Devuelve únicamente los programas activos pertenecientes a una academia específica"
    )
    @GetMapping("/academia/{academiaId}")
    public List<ProgramaResponseDTO> findByAcademia(@PathVariable Long academiaId) {
        return programaService.findByAcademiaId(academiaId);
    }

    @Operation(
            summary = "Crear programa",
            description = "Crea un nuevo programa asociado a una academia"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Programa creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Academia no encontrada")
    })
    @PostMapping("/academia/{academiaId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ProgramaResponseDTO create(
            @PathVariable Long academiaId,
            @RequestBody ProgramaCreateDTO dto
    ) {
        return programaService.create(academiaId, dto);
    }

    @Operation(
            summary = "Actualizar programa",
            description = "Actualiza nombre o descripción de un programa existente (no cambia academia)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Programa actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Programa no encontrado")
    })
    @PatchMapping("/{programaId}")
    public ProgramaResponseDTO update(
            @PathVariable Long programaId,
            @RequestBody ProgramaUpdateDTO dto
    ) {
        return programaService.update(programaId, dto);
    }

    @Operation(
            summary = "Eliminar programa (borrado lógico)",
            description = "Realiza un borrado lógico del programa (activo = false)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Programa desactivado correctamente"),
            @ApiResponse(responseCode = "404", description = "Programa no encontrado")
    })
    @DeleteMapping("/{programaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void softDelete(@PathVariable Long programaId) {
        programaService.softDelete(programaId);
    }
}
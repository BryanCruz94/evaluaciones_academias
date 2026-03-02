package com.nairbdev.academiasbackend.controller;

import com.nairbdev.academiasbackend.dto.programaPruebaFisica.ProgramaPruebaFisicaItemDTO;
import com.nairbdev.academiasbackend.dto.programaPruebaFisica.ProgramaPruebaFisicaResponseDTO;
import com.nairbdev.academiasbackend.dto.programaPruebaFisica.ProgramaPruebasFisicasBulkSaveRequest;
import com.nairbdev.academiasbackend.service.ProgramaPruebaFisicaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

@RestController
@RequestMapping("/api-registro")
@RequiredArgsConstructor
@Tag(
        name = "Programa - Pruebas Físicas",
        description = "Asignación y gestión de pruebas físicas dentro de un programa específico"
)
public class ProgramaPruebaFisicaController {

    private final ProgramaPruebaFisicaService service;

    @Operation(
            summary = "Listar pruebas físicas de un programa",
            description = "Devuelve las pruebas físicas activas asignadas a un programa dentro de una academia"
    )
    @GetMapping("/academias/{academiaId}/programas/{programaId}/pruebas-fisicas")
    public List<ProgramaPruebaFisicaResponseDTO> listar(
            @PathVariable Long academiaId,
            @PathVariable Long programaId
    ) {
        return service.listarActivas(academiaId, programaId);
    }

    @Operation(
            summary = "Guardar pruebas físicas (Bulk)",
            description = "Guarda o actualiza masivamente las pruebas físicas asociadas a un programa"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pruebas físicas guardadas correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Academia o programa no encontrado")
    })
    @PutMapping("/academias/{academiaId}/programas/{programaId}/pruebas-fisicas")
    public List<ProgramaPruebaFisicaResponseDTO> guardarBulk(
            @PathVariable Long academiaId,
            @PathVariable Long programaId,
            @RequestBody ProgramaPruebasFisicasBulkSaveRequest request
    ) {
        return service.guardarBulk(academiaId, programaId, request);
    }

    @Operation(
            summary = "Editar prueba física de un programa",
            description = "Actualiza una prueba física específica dentro de un programa"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prueba actualizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Registro no encontrado")
    })
    @PutMapping("/academias/{academiaId}/programas/{programaId}/pruebas-fisicas/{ppfId}")
    public ProgramaPruebaFisicaResponseDTO editar(
            @PathVariable Long academiaId,
            @PathVariable Long programaId,
            @PathVariable Long ppfId,
            @RequestBody ProgramaPruebaFisicaItemDTO item
    ) {
        return service.editar(academiaId, programaId, ppfId, item);
    }

    @Operation(
            summary = "Eliminar prueba física de un programa",
            description = "Realiza un borrado lógico de una prueba física dentro de un programa"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Prueba eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Registro no encontrado")
    })
    @DeleteMapping("/academias/{academiaId}/programas/{programaId}/pruebas-fisicas/{ppfId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(
            @PathVariable Long academiaId,
            @PathVariable Long programaId,
            @PathVariable Long ppfId
    ) {
        service.eliminar(academiaId, programaId, ppfId);
    }
}
package com.nairbdev.academiasbackend.controller;

import com.nairbdev.academiasbackend.dto.programaPruebaFisica.ProgramaPruebaFisicaItemDTO;
import com.nairbdev.academiasbackend.dto.programaPruebaFisica.ProgramaPruebaFisicaResponseDTO;
import com.nairbdev.academiasbackend.dto.programaPruebaFisica.ProgramaPruebasFisicasBulkSaveRequest;
import com.nairbdev.academiasbackend.service.ProgramaPruebaFisicaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api-registro")
@RequiredArgsConstructor
public class ProgramaPruebaFisicaController {

    private final ProgramaPruebaFisicaService service;

    // Abrir ventana: trae lista del programa
    @GetMapping("/academias/{academiaId}/programas/{programaId}/pruebas-fisicas")
    public List<ProgramaPruebaFisicaResponseDTO> listar(
            @PathVariable Long academiaId,
            @PathVariable Long programaId
    ) {
        return service.listarActivas(academiaId, programaId);
    }

    // Botón "Guardar pruebas físicas" (bulk)
    @PutMapping("/academias/{academiaId}/programas/{programaId}/pruebas-fisicas")
    public List<ProgramaPruebaFisicaResponseDTO> guardarBulk(
            @PathVariable Long academiaId,
            @PathVariable Long programaId,
            @RequestBody ProgramaPruebasFisicasBulkSaveRequest request
    ) {
        return service.guardarBulk(academiaId, programaId, request);
    }

    // Editar una fila (si quieres endpoint dedicado)
    @PutMapping("/academias/{academiaId}/programas/{programaId}/pruebas-fisicas/{ppfId}")
    public ProgramaPruebaFisicaResponseDTO editar(
            @PathVariable Long academiaId,
            @PathVariable Long programaId,
            @PathVariable Long ppfId,
            @RequestBody ProgramaPruebaFisicaItemDTO item
    ) {
        return service.editar(academiaId, programaId, ppfId, item);
    }

    // Eliminar una fila (borrado lógico)
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
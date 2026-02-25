package com.nairbdev.academiasbackend.controller;

import com.nairbdev.academiasbackend.dto.programas.ProgramaCreateDTO;
import com.nairbdev.academiasbackend.dto.programas.ProgramaResponseDTO;
import com.nairbdev.academiasbackend.dto.programas.ProgramaUpdateDTO;
import com.nairbdev.academiasbackend.service.ProgramaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api-registro/programas")
@RequiredArgsConstructor
public class ProgramaController {

    private final ProgramaService programaService;

    // 1) Todos los programas
    @GetMapping
    public List<ProgramaResponseDTO> findAll() {
        return programaService.findAll();
    }

    // 2) Programas por academia (solo los activos)
    @GetMapping("/academia/{academiaId}")
    public List<ProgramaResponseDTO> findByAcademia(@PathVariable Long academiaId) {
        return programaService.findByAcademiaId(academiaId);
    }

    // 3) Crear programa desde una academia
    @PostMapping("/academia/{academiaId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ProgramaResponseDTO create(
            @PathVariable Long academiaId,
            @RequestBody ProgramaCreateDTO dto
    ) {
        return programaService.create(academiaId, dto);
    }

    @PatchMapping("/{programaId}")
    public ProgramaResponseDTO update(
            @PathVariable Long programaId,
            @RequestBody ProgramaUpdateDTO dto
    ) {
        return programaService.update(programaId, dto);
    }

    @DeleteMapping("/{programaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void softDelete(@PathVariable Long programaId) {
        programaService.softDelete(programaId);
    }
}
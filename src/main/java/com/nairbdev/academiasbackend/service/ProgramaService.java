package com.nairbdev.academiasbackend.service;

import com.nairbdev.academiasbackend.dto.programas.ProgramaCreateDTO;
import com.nairbdev.academiasbackend.dto.programas.ProgramaResponseDTO;
import com.nairbdev.academiasbackend.dto.programas.ProgramaUpdateDTO;

import java.util.List;

public interface ProgramaService {
    List<ProgramaResponseDTO> findAll();
    List<ProgramaResponseDTO> findByAcademiaId(Long academiaId);
    ProgramaResponseDTO create(Long academiaId, ProgramaCreateDTO dto);
    ProgramaResponseDTO update(Long programaId, ProgramaUpdateDTO dto);
    void softDelete(Long programaId);
}
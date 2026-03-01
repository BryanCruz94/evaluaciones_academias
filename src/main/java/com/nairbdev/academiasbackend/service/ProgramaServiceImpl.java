package com.nairbdev.academiasbackend.service;

import com.nairbdev.academiasbackend.dto.programas.ProgramaResponseDTO;
import com.nairbdev.academiasbackend.dto.programas.ProgramaCreateDTO;
import com.nairbdev.academiasbackend.dto.programas.ProgramaUpdateDTO;
import com.nairbdev.academiasbackend.entity.Academia;
import com.nairbdev.academiasbackend.entity.Genero;
import com.nairbdev.academiasbackend.entity.Programa;
import com.nairbdev.academiasbackend.repository.ProgramaRepository;
import com.nairbdev.academiasbackend.repository.AcademiaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgramaServiceImpl implements ProgramaService {

    private final ProgramaRepository programaRepository;
    private final AcademiaRepository academiaRepository;

    @Override
    public List<ProgramaResponseDTO> findAll() {
        return programaRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<ProgramaResponseDTO> findByAcademiaId(Long academiaId) {
        return programaRepository.findByAcademiaIdAndActivoTrue(academiaId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    private ProgramaResponseDTO toDto(Programa p) {
        return new ProgramaResponseDTO(
                p.getId(),
                (p.getAcademia() != null) ? p.getAcademia().getNombre() : null,
                p.getNombre(),
                p.getDescripcion(),
                p.getGenero(Genero.M).name(),
                p.getActivo()
        );
    }

    @Override
    public ProgramaResponseDTO create(Long academiaId, ProgramaCreateDTO dto) {

        Academia academia = academiaRepository.findById(academiaId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe la academia con id: " + academiaId
                ));
        Genero genero = Genero.valueOf(dto.genero());

        Programa programa = new Programa();
        programa.setAcademia(academia);
        programa.setNombre(dto.nombre());
        programa.setDescripcion(dto.descripcion());
        programa.setGenero(genero);
        programa.setActivo(dto.activo() != null ? dto.activo() : true);

        Programa saved = programaRepository.save(programa);

        return new ProgramaResponseDTO(
                saved.getId(),
                saved.getAcademia() != null ? saved.getAcademia().getNombre() : null,
                saved.getNombre(),
                saved.getDescripcion(),
                saved.getGenero(genero).name(),
                saved.getActivo()
        );
    }

    @Override
    public ProgramaResponseDTO update(Long programaId, ProgramaUpdateDTO dto) {

        Programa programa = programaRepository.findById(programaId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe el programa con id: " + programaId
                ));

        // Academia NO se toca

        if (dto.nombre() != null) {
            programa.setNombre(dto.nombre().trim());
        }
        if (dto.descripcion() != null) {
            programa.setDescripcion(dto.descripcion().trim());
        }

        Programa saved = programaRepository.save(programa);

        Genero genero = Genero.valueOf(dto.genero());

        return new ProgramaResponseDTO(
                saved.getId(),
                saved.getAcademia() != null ? saved.getAcademia().getNombre() : null,
                saved.getNombre(),
                saved.getDescripcion(),
                saved.getGenero(genero).name(),
                saved.getActivo()
        );
    }

    @Override
    public void softDelete(Long programaId) {

        Programa programa = programaRepository.findById(programaId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe el programa con id: " + programaId
                ));

        if (Boolean.FALSE.equals(programa.getActivo())) {
            // ya está inactivo, no hacemos nada (idempotente)
            return;
        }

        programa.setActivo(false);
        programaRepository.save(programa);
    }
}
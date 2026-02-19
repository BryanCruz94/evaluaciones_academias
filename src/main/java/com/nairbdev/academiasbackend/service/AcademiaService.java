package com.nairbdev.academiasbackend.service;

import com.nairbdev.academiasbackend.entity.Academia;
import com.nairbdev.academiasbackend.repository.AcademiaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AcademiaService {

    private final AcademiaRepository academiaRepository;

    public AcademiaService(AcademiaRepository academiaRepository) {
        this.academiaRepository = academiaRepository;
    }

    public List<Academia> listarTodas() {
        return academiaRepository.findAll();
    }

    public List<Academia> listarActivas() {
        return academiaRepository.findByActivoTrue();
    }

    public Academia buscarPorCodigo(String codigo) {
        return academiaRepository.findByCodigo(codigo)
                .orElseThrow(() -> new IllegalArgumentException("No existe academia con código: " + codigo));
    }
}

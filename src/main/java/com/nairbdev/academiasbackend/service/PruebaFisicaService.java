package com.nairbdev.academiasbackend.service;

import com.nairbdev.academiasbackend.dto.programaPruebaFisica.ProgramaPruebaFisicaCreateRequest;
import com.nairbdev.academiasbackend.dto.programaPruebaFisica.ProgramaPruebaFisicaResponse;
import com.nairbdev.academiasbackend.entity.PruebaFisica;

import java.util.List;

public interface PruebaFisicaService {

    List<PruebaFisica> listarCatalogoActivas(Long academiaId);

    List<ProgramaPruebaFisicaResponse> listarAsignadasPorPrograma(Long academiaId, Long programaId);

    ProgramaPruebaFisicaResponse agregarPruebaAFPrograma(Long academiaId, Long programaId, ProgramaPruebaFisicaCreateRequest req);

    void eliminarAsignacion(Long academiaId, Long programaPruebaFisicaId);
}
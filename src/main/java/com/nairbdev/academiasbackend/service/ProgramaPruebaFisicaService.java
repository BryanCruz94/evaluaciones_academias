package com.nairbdev.academiasbackend.service;

import com.nairbdev.academiasbackend.dto.programaPruebaFisica.ProgramaPruebaFisicaItemDTO;
import com.nairbdev.academiasbackend.dto.programaPruebaFisica.ProgramaPruebaFisicaResponseDTO;
import com.nairbdev.academiasbackend.dto.programaPruebaFisica.ProgramaPruebasFisicasBulkSaveRequest;
import com.nairbdev.academiasbackend.entity.OperadorObjetivo;
import com.nairbdev.academiasbackend.entity.Programa;
import com.nairbdev.academiasbackend.entity.ProgramaPruebaFisica;
import com.nairbdev.academiasbackend.entity.TipoValorPrueba;
import com.nairbdev.academiasbackend.repository.ProgramaPruebaFisicaRepository;
import com.nairbdev.academiasbackend.repository.ProgramaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgramaPruebaFisicaService {

    private final ProgramaRepository programaRepository;
    private final ProgramaPruebaFisicaRepository repo;

    @Transactional(readOnly = true)
    public List<ProgramaPruebaFisicaResponseDTO> listarActivas(Long academiaId, Long programaId) {
        Programa programa = validarPrograma(academiaId, programaId);

        return repo.findByProgramaIdAndActivoTrueOrderByIdAsc(programa.getId())
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional
    public List<ProgramaPruebaFisicaResponseDTO> guardarBulk(Long academiaId, Long programaId, ProgramaPruebasFisicasBulkSaveRequest request) {
        Programa programa = validarPrograma(academiaId, programaId);

        if (request == null || request.items() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "items es obligatorio.");
        }

        List<ProgramaPruebaFisicaResponseDTO> out = new ArrayList<>();

        for (ProgramaPruebaFisicaItemDTO item : request.items()) {
            validarItem(item);

            ProgramaPruebaFisica entity;
            if (item.id() == null) {
                entity = new ProgramaPruebaFisica();
                entity.setPrograma(programa);
            } else {
                entity = repo.findByIdAndProgramaId(item.id(), programa.getId())
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "No existe una prueba con id " + item.id() + " para este programa."
                        ));
            }

            entity.setNombre(item.nombre().trim());
            entity.setDescripcion(item.descripcion());
            entity.setTipoValor(item.tipoValor());
            entity.setUnidad(item.unidad());

            // ✅ normaliza objetivo según tipo
            NormalizedGoal ng = normalizeGoal(item);

            entity.setObjetivoValor(ng.objetivoValor());
            entity.setOperador(ng.operadorFinal()); // puede venir forzado (BOOLEANO)
            entity.setEtiqueta(item.etiqueta());
            entity.setActivo(item.activo() != null ? item.activo() : true);

            // regla: objetivoValor y operador deben venir ambos o ambos null
            boolean ok = (entity.getObjetivoValor() == null && entity.getOperador() == null)
                    || (entity.getObjetivoValor() != null && entity.getOperador() != null);
            if (!ok) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "objetivoValor y operador deben venir ambos o ambos null. Prueba: " + entity.getNombre()
                );
            }

            ProgramaPruebaFisica saved = repo.save(entity);
            out.add(toDto(saved));
        }

        return out;
    }

    @Transactional
    public ProgramaPruebaFisicaResponseDTO editar(Long academiaId, Long programaId, Long ppfId, ProgramaPruebaFisicaItemDTO item) {
        validarPrograma(academiaId, programaId);
        validarItem(item);

        ProgramaPruebaFisica entity = repo.findByIdAndProgramaId(ppfId, programaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prueba no encontrada."));

        entity.setNombre(item.nombre().trim());
        entity.setDescripcion(item.descripcion());
        entity.setTipoValor(item.tipoValor());
        entity.setUnidad(item.unidad());

        NormalizedGoal ng = normalizeGoal(item);

        entity.setObjetivoValor(ng.objetivoValor());
        entity.setOperador(ng.operadorFinal());
        entity.setEtiqueta(item.etiqueta());

        boolean ok = (entity.getObjetivoValor() == null && entity.getOperador() == null)
                || (entity.getObjetivoValor() != null && entity.getOperador() != null);
        if (!ok) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "objetivoValor y operador deben venir ambos o ambos null.");
        }

        return toDto(repo.save(entity));
    }

    @Transactional
    public void eliminar(Long academiaId, Long programaId, Long ppfId) {
        validarPrograma(academiaId, programaId);

        ProgramaPruebaFisica entity = repo.findByIdAndProgramaId(ppfId, programaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prueba no encontrada."));

        entity.setActivo(false);
        repo.save(entity);
    }

    // ---------------- helpers ----------------

    private Programa validarPrograma(Long academiaId, Long programaId) {
        Programa programa = programaRepository.findById(programaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Programa no encontrado."));

        if (programa.getAcademia() == null || !programa.getAcademia().getId().equals(academiaId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El programa no pertenece a la academia indicada.");
        }
        if (Boolean.FALSE.equals(programa.getActivo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El programa está inactivo.");
        }
        return programa;
    }

    private void validarItem(ProgramaPruebaFisicaItemDTO item) {
        if (item == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "item no puede ser null.");
        if (item.nombre() == null || item.nombre().isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "nombre es obligatorio.");
        if (item.tipoValor() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "tipoValor es obligatorio.");

        // Validación de "solo un objetivo según tipo"
        boolean hasValor = item.objetivoValor() != null;
        boolean hasTiempo = item.objetivoTiempo() != null && !item.objetivoTiempo().isBlank();
        boolean hasBool = item.objetivoBooleano() != null;

        switch (item.tipoValor()) {
            case TIEMPO -> {
                if (hasBool) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "objetivoBooleano no aplica para TIEMPO.");
                if (hasTiempo && hasValor) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "En TIEMPO envía objetivoTiempo o objetivoValor (segundos), no ambos.");
                }
            }
            case BOOLEANO -> {
                if (hasTiempo) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "objetivoTiempo no aplica para BOOLEANO.");
                if (hasValor) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "En BOOLEANO envía objetivoBooleano (true/false), no objetivoValor.");
            }
            default -> { // NUMERICO / REPETICIONES
                if (hasTiempo) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "objetivoTiempo solo aplica para TIEMPO.");
                if (hasBool) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "objetivoBooleano solo aplica para BOOLEANO.");
            }
        }

        // si envías operador, debes enviar objetivo correspondiente (lo reforzamos aquí con mensaje claro)
        if (item.operador() != null) {
            boolean tieneAlgunaMeta = hasValor || hasTiempo || hasBool;
            if (!tieneAlgunaMeta) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Si envías operador, debes enviar un objetivo.");
            }
        }
    }

    /**
     * Normaliza el objetivo según el tipo:
     * - TIEMPO: "mm:ss" o "hh:mm:ss" => BigDecimal(segundos)
     * - BOOLEANO: true/false => 1/0 y operador forzado a EQ si hay objetivo
     * - Otros: objetivoValor tal cual
     */
    private NormalizedGoal normalizeGoal(ProgramaPruebaFisicaItemDTO item) {
        if (item.tipoValor() == TipoValorPrueba.TIEMPO) {
            if (item.objetivoValor() != null) {
                // compatibilidad: si ya te mandan segundos numéricos
                if (item.objetivoValor().compareTo(BigDecimal.ZERO) < 0) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "objetivoValor (segundos) no puede ser negativo.");
                }
                return new NormalizedGoal(item.objetivoValor(), item.operador());
            }

            String t = item.objetivoTiempo();
            if (t == null || t.isBlank()) {
                return new NormalizedGoal(null, item.operador()); // permite null si operador también es null, se valida luego
            }

            long segundos = parseTiempoASegundos(t.trim());
            return new NormalizedGoal(BigDecimal.valueOf(segundos), item.operador());
        }

        if (item.tipoValor() == TipoValorPrueba.BOOLEANO) {
            if (item.objetivoBooleano() == null) {
                // permite null si operador es null (sin objetivo)
                return new NormalizedGoal(null, item.operador());
            }
            BigDecimal v = item.objetivoBooleano() ? BigDecimal.ONE : BigDecimal.ZERO;

            // ✅ para SI/NO lo más lógico es igualdad
            OperadorObjetivo op = (item.operador() != null) ? item.operador() : OperadorObjetivo.EQ;
            if (op != OperadorObjetivo.EQ) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Para BOOLEANO el operador debe ser '=' (EQ).");
            }

            return new NormalizedGoal(v, op);
        }

        // NUMERICO / REPETICIONES
        return new NormalizedGoal(item.objetivoValor(), item.operador());
    }

    private long parseTiempoASegundos(String value) {
        String[] parts = value.split(":");
        try {
            if (parts.length == 2) {
                int mm = Integer.parseInt(parts[0].trim());
                int ss = Integer.parseInt(parts[1].trim());
                validarRangoTiempo(mm, ss);
                return (long) mm * 60 + ss;
            }
            if (parts.length == 3) {
                int hh = Integer.parseInt(parts[0].trim());
                int mm = Integer.parseInt(parts[1].trim());
                int ss = Integer.parseInt(parts[2].trim());
                if (hh < 0) throw new IllegalArgumentException();
                validarRangoTiempo(mm, ss);
                return (long) hh * 3600 + (long) mm * 60 + ss;
            }
        } catch (Exception ignored) {}

        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Formato de objetivoTiempo inválido. Usa mm:ss (12:10) o hh:mm:ss (01:12:10)."
        );
    }

    private void validarRangoTiempo(int mm, int ss) {
        if (mm < 0 || ss < 0 || ss > 59) throw new IllegalArgumentException();
    }

    private ProgramaPruebaFisicaResponseDTO toDto(ProgramaPruebaFisica e) {

        String tiempoFormateado = null;
        Boolean booleanoFormateado = null;

        if (e.getObjetivoValor() != null) {

            switch (e.getTipoValor()) {

                case TIEMPO -> {
                    long totalSegundos = e.getObjetivoValor().longValue();
                    tiempoFormateado = formatSegundos(totalSegundos);
                }

                case BOOLEANO -> {
                    booleanoFormateado = e.getObjetivoValor().compareTo(BigDecimal.ONE) == 0;
                }

                default -> {
                    // REPETICIONES / NUMERICO no necesitan formato adicional
                }
            }
        }

        return new ProgramaPruebaFisicaResponseDTO(
                e.getId(),
                e.getPrograma().getId(),
                e.getNombre(),
                e.getDescripcion(),
                e.getTipoValor(),
                e.getUnidad(),
                e.getObjetivoValor(),      // valor crudo
                tiempoFormateado,          // tiempo formateado
                booleanoFormateado,        // booleano formateado
                e.getOperador(),
                e.getEtiqueta(),
                e.getActivo()
        );
    }

    private record NormalizedGoal(BigDecimal objetivoValor, OperadorObjetivo operadorFinal) {}

    private String formatSegundos(long totalSegundos) {

        long horas = totalSegundos / 3600;
        long minutos = (totalSegundos % 3600) / 60;
        long segundos = totalSegundos % 60;

        if (horas > 0) {
            return String.format("%02d:%02d:%02d", horas, minutos, segundos);
        }

        return String.format("%02d:%02d", minutos, segundos);
    }
}


package com.nairbdev.academiasbackend.service;

import com.nairbdev.academiasbackend.dto.jornadaFisica.*;
import com.nairbdev.academiasbackend.dto.pruebasFisicas.PruebaFisicaBateriaResumenDTO;
import com.nairbdev.academiasbackend.dto.pruebasFisicas.PruebaFisicaFilaDTO;
import com.nairbdev.academiasbackend.dto.pruebasFisicas.PruebaFisicaValorDTO;
import com.nairbdev.academiasbackend.dto.pruebasFisicas.PruebasFisicasTablaAlumnoDTO;
import com.nairbdev.academiasbackend.entity.*;
import com.nairbdev.academiasbackend.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JornadaFisicaService {

    private final AlumnoRepository alumnoRepository;
    private final ProgramaPruebaFisicaRepository programaPruebaFisicaRepository;
    private final JornadaFisicaRepository jornadaFisicaRepository;
    private final ResultadoFisicoRepository resultadoFisicoRepository;
    private final UsuarioRepository usuarioRepository;

    public JornadaFisicaService(AlumnoRepository alumnoRepository,
                                ProgramaPruebaFisicaRepository programaPruebaFisicaRepository,
                                JornadaFisicaRepository jornadaFisicaRepository,
                                ResultadoFisicoRepository resultadoFisicoRepository,
                                UsuarioRepository usuarioRepository) {
        this.alumnoRepository = alumnoRepository;
        this.programaPruebaFisicaRepository = programaPruebaFisicaRepository;
        this.jornadaFisicaRepository = jornadaFisicaRepository;
        this.resultadoFisicoRepository = resultadoFisicoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public JornadaFisicaResponseDTO registrarJornadaFisica(Long academiaId, Long alumnoId, Long evaluadorId,
                                                           JornadaFisicaCreateRequest req) {

        if (req == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Body obligatorio.");
        }
        if (req.fechaEvaluacion() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fechaEvaluacion es obligatoria.");
        }
        if (req.pesoKg() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El pesoKg es obligatorio.");
        }
        if (req.estaturaCm() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La estaturaCm es obligatoria.");
        }
        if (req.resultados() == null || req.resultados().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe enviar resultados (lista no vacía).");
        }

        // Alumno
        Alumno alumno = alumnoRepository.findByIdConTodo(alumnoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Alumno no encontrado."));

        if (!alumno.getAcademia().getId().equals(academiaId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Alumno no pertenece a la academia indicada.");
        }

        Programa programa = alumno.getProgramaActual();
        if (programa == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El alumno no tiene programa actual asignado.");
        }
        if (!programa.getAcademia().getId().equals(academiaId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "El programa actual del alumno no pertenece a la academia indicada.");
        }

        // Evaluador (id viene en URL)
        Usuario evaluador = usuarioRepository.findByIdAndActivoTrue(evaluadorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evaluador no encontrado o inactivo."));

        LocalDate fecha = req.fechaEvaluacion();

        // Unicidad alumno+fecha (evitar reventar por constraint)
        jornadaFisicaRepository.findByAlumnoIdAndFecha(alumnoId, fecha)
                .ifPresent(j -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT,
                            "Ya existe una jornada física para este alumno en la fecha " + fecha + ".");
                });

        // Traer pruebas del programa (activas)
        List<ProgramaPruebaFisica> pruebas = programaPruebaFisicaRepository
                .findByProgramaIdAndActivoTrueOrderByIdAsc(programa.getId());

        if (pruebas.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "El programa no tiene pruebas físicas activas configuradas.");
        }

        Map<Long, ProgramaPruebaFisica> pruebasPorId = new HashMap<>();
        for (ProgramaPruebaFisica p : pruebas) {
            pruebasPorId.put(p.getId(), p);
        }

        // Validar ids repetidos + pertenencia al programa
        Set<Long> vistos = new HashSet<>();
        for (ResultadoFisicoCreateItem item : req.resultados()) {
            if (item.programaPruebaFisicaId() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "programaPruebaFisicaId es obligatorio en cada item.");
            }
            if (!vistos.add(item.programaPruebaFisicaId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "programaPruebaFisicaId repetido: " + item.programaPruebaFisicaId());
            }
            if (!pruebasPorId.containsKey(item.programaPruebaFisicaId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "La prueba " + item.programaPruebaFisicaId()
                                + " no pertenece al programa actual del alumno o no está activa.");
            }
        }

        // Calcular IMC
        BigDecimal imc = calcularImc(req.pesoKg(), req.estaturaCm());

        // Crear Jornada
        JornadaFisica jornada = new JornadaFisica();
        jornada.setAlumno(alumno);
        jornada.setAcademia(alumno.getAcademia());
        jornada.setPrograma(programa);
        jornada.setFecha(fecha);
        jornada.setEvaluador(evaluador);
        jornada.setObservacion(req.observacion());
        jornada.setPesoKg(BigDecimal.valueOf(req.pesoKg()).setScale(2, RoundingMode.HALF_UP));
        jornada.setImc(imc);

        jornada = jornadaFisicaRepository.save(jornada);

        // Crear Resultados
        List<ResultadoFisico> guardados = new ArrayList<>();
        for (ResultadoFisicoCreateItem item : req.resultados()) {
            ProgramaPruebaFisica ppf = pruebasPorId.get(item.programaPruebaFisicaId());

            validarValorSegunTipo(ppf.getTipoValor(), item);

            ResultadoFisico rf = new ResultadoFisico();
            rf.setJornadaFisica(jornada);
            rf.setProgramaPruebaFisica(ppf);
            rf.setValorNum(item.valorNum());
            rf.setValorBool(item.valorBool());
            rf.setObservacion(item.observacion());

            guardados.add(resultadoFisicoRepository.save(rf));
        }

        // Actualizar alumno (peso + imc + estatura desde front)
        alumno.setPesoKg(req.pesoKg());
        alumno.setImc(imc);
        alumno.setEstaturaCm(req.estaturaCm());
        alumnoRepository.save(alumno);

        return toResponse(jornada, guardados);
    }

    private void validarValorSegunTipo(TipoValorPrueba tipo, ResultadoFisicoCreateItem item) {
        switch (tipo) {
            case BOOLEANO -> {
                if (item.valorBool() == null) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "La prueba BOOLEANO requiere valorBool (true/false).");
                }
                if (item.valorNum() != null) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "La prueba BOOLEANO no debe enviar valorNum.");
                }
            }
            case TIEMPO, REPETICIONES, NUMERICO -> {
                if (item.valorNum() == null) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "La prueba " + tipo + " requiere valorNum.");
                }
                if (item.valorBool() != null) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "La prueba " + tipo + " no debe enviar valorBool.");
                }
            }
        }
    }

    private BigDecimal calcularImc(Double pesoKg, Double estaturaCm) {
        if (pesoKg == null || estaturaCm == null || pesoKg <= 0 || estaturaCm <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "pesoKg y estaturaCm deben ser > 0.");
        }

        BigDecimal peso = BigDecimal.valueOf(pesoKg);

        BigDecimal estaturaM = BigDecimal.valueOf(estaturaCm)
                .divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP);

        BigDecimal estatura2 = estaturaM.multiply(estaturaM);

        return peso.divide(estatura2, 2, RoundingMode.HALF_UP);
    }

    private JornadaFisicaResponseDTO toResponse(JornadaFisica j, List<ResultadoFisico> resultados) {
        List<ResultadoFisicoResponseItem> items = resultados.stream()
                .map(r -> {
                    ProgramaPruebaFisica p = r.getProgramaPruebaFisica();
                    String tiempoFmt = null;
                    if (p.getTipoValor() == TipoValorPrueba.TIEMPO && r.getValorNum() != null) {
                        tiempoFmt = formatMinSec(r.getValorNum());
                    }
                    return new ResultadoFisicoResponseItem(
                            r.getId(),
                            p.getId(),
                            p.getNombre(),
                            p.getTipoValor(),
                            p.getUnidad(),
                            r.getValorNum(),
                            tiempoFmt,
                            r.getValorBool(),
                            r.getObservacion()
                    );
                })
                .toList();

        return new JornadaFisicaResponseDTO(
                j.getId(),
                j.getAlumno().getId(),
                j.getPrograma().getId(),
                j.getFecha(),
                j.getEvaluador().getId(),
                j.getObservacion(),
                j.getPesoKg() != null ? j.getPesoKg().doubleValue() : null,
                j.getImc(),
                items
        );
    }

    // TIEMPO asumido en segundos: 75 => 01:15
    private String formatMinSec(BigDecimal segundos) {
        long s = segundos.setScale(0, RoundingMode.HALF_UP).longValue();
        long min = s / 60;
        long sec = s % 60;
        return String.format("%02d:%02d", min, sec);
    }

    @Transactional(readOnly = true)
    public PruebasFisicasTablaAlumnoDTO obtenerTablaBateriasPorAlumno(Long academiaId, Long alumnoId) {
        Alumno alumno = alumnoRepository.findByIdConTodo(alumnoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Alumno no encontrado."));

        if (!alumno.getAcademia().getId().equals(academiaId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Alumno no pertenece a la academia indicada.");
        }

        List<JornadaFisica> jornadas = jornadaFisicaRepository.findByAlumnoIdOrderByFechaDesc(alumnoId);
        if (jornadas.isEmpty()) {
            return new PruebasFisicasTablaAlumnoDTO(alumnoId, List.of(), List.of(), null, List.of(), List.of());
        }

        List<JornadaFisica> jornadasAsc = new ArrayList<>(jornadas);
        jornadasAsc.sort(Comparator.comparing(JornadaFisica::getFecha));

        List<Long> jornadaIds = jornadasAsc.stream().map(JornadaFisica::getId).toList();
        Map<Long, List<ResultadoFisico>> resultadosPorJornada = resultadoFisicoRepository
                .findByJornadaFisicaIdIn(jornadaIds)
                .stream()
                .collect(Collectors.groupingBy(r -> r.getJornadaFisica().getId()));

        Map<Long, ProgramaPruebaFisica> pruebasMap = new LinkedHashMap<>();
        for (List<ResultadoFisico> resultados : resultadosPorJornada.values()) {
            resultados.stream()
                    .sorted(Comparator.comparing(r -> r.getProgramaPruebaFisica().getId()))
                    .forEach(r -> pruebasMap.putIfAbsent(r.getProgramaPruebaFisica().getId(), r.getProgramaPruebaFisica()));
        }

        List<Long> baterias = jornadaIds;
        List<LocalDate> fechas = jornadasAsc.stream().map(JornadaFisica::getFecha).toList();

        List<PruebaFisicaFilaDTO> filas = new ArrayList<>();
        filas.add(new PruebaFisicaFilaDTO(
                "Fecha",
                null,
                jornadasAsc.stream()
                        .map(j -> new PruebaFisicaValorDTO(j.getId(), null, null, null, j.getFecha().toString()))
                        .toList()
        ));

        List<ProgramaPruebaFisica> pruebasOrdenadas = new ArrayList<>(pruebasMap.values());
        pruebasOrdenadas.sort(Comparator.comparing(ProgramaPruebaFisica::getId));

        for (ProgramaPruebaFisica prueba : pruebasOrdenadas) {
            List<PruebaFisicaValorDTO> valoresFila = new ArrayList<>();
            for (JornadaFisica jornada : jornadasAsc) {
                ResultadoFisico resultado = resultadosPorJornada.getOrDefault(jornada.getId(), List.of())
                        .stream()
                        .filter(r -> r.getProgramaPruebaFisica().getId().equals(prueba.getId()))
                        .findFirst()
                        .orElse(null);

                valoresFila.add(new PruebaFisicaValorDTO(
                        jornada.getId(),
                        prueba.getObjetivoValor() != null ? prueba.getObjetivoValor().doubleValue() : null,
                        resultado != null && resultado.getValorNum() != null ? resultado.getValorNum().doubleValue() : null,
                        resultado != null ? resultado.getValorBool() : null,
                        null
                ));
            }
            filas.add(new PruebaFisicaFilaDTO(prueba.getNombre(), prueba.getUnidad(), valoresFila));
        }

        ProgramaPruebaFisica ultimaPrueba = pruebasOrdenadas.isEmpty() ? null : pruebasOrdenadas.get(pruebasOrdenadas.size() - 1);
        List<PruebaFisicaBateriaResumenDTO> resumenFinal = new ArrayList<>();
        if (ultimaPrueba != null) {
            for (JornadaFisica jornada : jornadasAsc) {
                ResultadoFisico ultimoResultado = resultadosPorJornada.getOrDefault(jornada.getId(), List.of())
                        .stream()
                        .filter(r -> r.getProgramaPruebaFisica().getId().equals(ultimaPrueba.getId()))
                        .findFirst()
                        .orElse(null);

                Boolean aprobado = evaluarAprobacion(ultimaPrueba, ultimoResultado);
                Double diferencia = calcularDiferencia(ultimaPrueba, ultimoResultado);

                resumenFinal.add(new PruebaFisicaBateriaResumenDTO(
                        jornada.getId(),
                        Boolean.TRUE.equals(aprobado) ? "✓" : "X",
                        aprobado,
                        diferencia
                ));
            }
        }

        return new PruebasFisicasTablaAlumnoDTO(
                alumnoId,
                baterias,
                fechas,
                ultimaPrueba != null ? ultimaPrueba.getNombre() : null,
                filas,
                resumenFinal
        );
    }

    private Boolean evaluarAprobacion(ProgramaPruebaFisica prueba, ResultadoFisico resultado) {
        if (prueba == null || resultado == null) {
            return false;
        }
        if (prueba.getTipoValor() == TipoValorPrueba.BOOLEANO) {
            return Boolean.TRUE.equals(resultado.getValorBool());
        }
        if (prueba.getObjetivoValor() == null || resultado.getValorNum() == null) {
            return false;
        }
        if (prueba.getOperador() == null) {
            return false;
        }

        return switch (prueba.getOperador()) {
            case GE -> resultado.getValorNum().compareTo(prueba.getObjetivoValor()) >= 0;
            case LE -> resultado.getValorNum().compareTo(prueba.getObjetivoValor()) <= 0;
            case EQ -> resultado.getValorNum().compareTo(prueba.getObjetivoValor()) == 0;
        };
    }

    private Double calcularDiferencia(ProgramaPruebaFisica prueba, ResultadoFisico resultado) {
        if (prueba == null || resultado == null || prueba.getObjetivoValor() == null || resultado.getValorNum() == null) {
            return null;
        }
        if (prueba.getOperador() == null) {
            return null;
        }

        BigDecimal diff = switch (prueba.getOperador()) {
            case GE -> resultado.getValorNum().subtract(prueba.getObjetivoValor());
            case LE -> prueba.getObjetivoValor().subtract(resultado.getValorNum());
            case EQ -> BigDecimal.ZERO.subtract(resultado.getValorNum().subtract(prueba.getObjetivoValor()).abs());
        };

        return diff != null ? diff.setScale(2, RoundingMode.HALF_UP).doubleValue() : null;
    }
}

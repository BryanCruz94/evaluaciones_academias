package com.nairbdev.academiasbackend.service;

import com.nairbdev.academiasbackend.dto.pruebasFisicas.AlumnoPublicoPruebasFisicasDTO;
import com.nairbdev.academiasbackend.dto.pruebasFisicas.ConsolidadoFisicoResponseDTO;
import com.nairbdev.academiasbackend.entity.*;
import com.nairbdev.academiasbackend.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JornadaFisicaServiceTest {

    private final AlumnoRepository alumnoRepository = mock(AlumnoRepository.class);
    private final ProgramaPruebaFisicaRepository programaPruebaFisicaRepository = mock(ProgramaPruebaFisicaRepository.class);
    private final JornadaFisicaRepository jornadaFisicaRepository = mock(JornadaFisicaRepository.class);
    private final ResultadoFisicoRepository resultadoFisicoRepository = mock(ResultadoFisicoRepository.class);
    private final UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
    private final ConsultaAlumnoAuditoriaService consultaAlumnoAuditoriaService =
            mock(ConsultaAlumnoAuditoriaService.class);

    private final JornadaFisicaService service = new JornadaFisicaService(
            alumnoRepository,
            programaPruebaFisicaRepository,
            jornadaFisicaRepository,
            resultadoFisicoRepository,
            usuarioRepository,
            consultaAlumnoAuditoriaService
    );

    @Test
    void consolidadoOrdenaFechasYAlumnosYCalculaAprobacionCompleta() {
        Academia academia = academia(1L);
        Programa programa = programa(10L, academia, "Aspirantes Policia");
        Alumno ana = alumno(100L, academia, programa, "Zambrano", "Ana");
        Alumno luis = alumno(200L, academia, programa, "Alvarez", "Luis");

        ProgramaPruebaFisica flexiones = prueba(1000L, programa, "Flexiones", TipoValorPrueba.REPETICIONES, OperadorObjetivo.GE, "20");
        ProgramaPruebaFisica carrera = prueba(2000L, programa, "Carrera", TipoValorPrueba.TIEMPO, OperadorObjetivo.LE, "720");

        JornadaFisica anaMarzo = jornada(500L, ana, academia, programa, LocalDate.of(2026, 3, 10));
        JornadaFisica luisEnero = jornada(600L, luis, academia, programa, LocalDate.of(2026, 1, 15));
        JornadaFisica luisMarzo = jornada(700L, luis, academia, programa, LocalDate.of(2026, 3, 10));

        when(jornadaFisicaRepository.findConsolidadoByAcademiaId(1L))
                .thenReturn(List.of(anaMarzo, luisEnero, luisMarzo));
        when(resultadoFisicoRepository.findByJornadaFisicaIdIn(List.of(500L, 600L, 700L)))
                .thenReturn(List.of(
                        resultado(anaMarzo, flexiones, "22"),
                        resultado(anaMarzo, carrera, "710"),
                        resultado(luisEnero, flexiones, "18"),
                        resultado(luisEnero, carrera, "730"),
                        resultado(luisMarzo, flexiones, "25")
                ));
        when(programaPruebaFisicaRepository.findByProgramaIdAndActivoTrueOrderByIdAsc(10L))
                .thenReturn(List.of(flexiones, carrera));

        ConsolidadoFisicoResponseDTO consolidado = service.obtenerConsolidadoFisicoPorAcademia(1L);

        assertThat(consolidado.fechas())
                .containsExactly(LocalDate.of(2026, 1, 15), LocalDate.of(2026, 3, 10));
        assertThat(consolidado.alumnos())
                .extracting("nombreCompleto")
                .containsExactly("Alvarez Luis", "Zambrano Ana");
        assertThat(consolidado.alumnos().get(0).evaluaciones())
                .extracting("estado")
                .containsExactly("No aprobado", "No aprobado");
        assertThat(consolidado.alumnos().get(1).evaluaciones())
                .extracting("estado")
                .containsExactly("", "Aprobado");
        assertThat(consolidado.alumnos().get(1).programa()).isEqualTo("Aspirantes Policia");
    }

    @Test
    void consolidadoVacioRetornaMatrizSinColumnasNiFilas() {
        when(jornadaFisicaRepository.findConsolidadoByAcademiaId(1L)).thenReturn(List.of());

        ConsolidadoFisicoResponseDTO consolidado = service.obtenerConsolidadoFisicoPorAcademia(1L);

        assertThat(consolidado.fechas()).isEmpty();
        assertThat(consolidado.alumnos()).isEmpty();
    }

    @Test
    void consolidadoMarcaNoAprobadoCuandoUnResultadoNoTienePruebaAsociada() {
        Academia academia = academia(1L);
        Programa programa = programa(10L, academia, "Aspirantes Bomberos");
        Alumno ana = alumno(100L, academia, programa, "Zambrano", "Ana");
        ProgramaPruebaFisica flexiones = prueba(1000L, programa, "Flexiones", TipoValorPrueba.REPETICIONES, OperadorObjetivo.GE, "20");
        JornadaFisica jornada = jornada(500L, ana, academia, programa, LocalDate.of(2026, 3, 10));
        ResultadoFisico resultadoIncompleto = new ResultadoFisico();
        resultadoIncompleto.setJornadaFisica(jornada);
        resultadoIncompleto.setValorNum(new BigDecimal("25"));

        when(jornadaFisicaRepository.findConsolidadoByAcademiaId(1L)).thenReturn(List.of(jornada));
        when(resultadoFisicoRepository.findByJornadaFisicaIdIn(List.of(500L)))
                .thenReturn(List.of(resultadoIncompleto));
        when(programaPruebaFisicaRepository.findByProgramaIdAndActivoTrueOrderByIdAsc(10L))
                .thenReturn(List.of(flexiones));

        ConsolidadoFisicoResponseDTO consolidado = service.obtenerConsolidadoFisicoPorAcademia(1L);

        assertThat(consolidado.alumnos().get(0).evaluaciones().get(0).estado())
                .isEqualTo("No aprobado");
    }

    @Test
    void consultaPublicaPorCedulaRetornaAlumnoYMatrizSinExponerEmail() {
        Academia academia = academia(1L);
        Programa programa = programa(10L, academia, "Aspirantes Policia");
        Alumno ana = alumno(100L, academia, programa, "Zambrano", "Ana");
        ana.setCedula("0102030405");
        ana.setFechaNacimiento(LocalDate.of(2010, 5, 20));
        ana.setEstaturaCm(165.0);
        ana.setPesoKg(62.5);
        ana.setImc(new BigDecimal("22.96"));
        ana.setFotoUrl("https://cdn.example.com/foto.jpg");

        when(alumnoRepository.findByCedulaConTodo("0102030405")).thenReturn(List.of(ana));
        when(alumnoRepository.findByIdConTodo(100L)).thenReturn(Optional.of(ana));
        when(jornadaFisicaRepository.findByAlumnoIdOrderByFechaDesc(100L)).thenReturn(List.of());

        AlumnoPublicoPruebasFisicasDTO response =
                service.obtenerMatrizFisicaPublicaPorCedula("0102030405");

        assertThat(response.id()).isEqualTo(100L);
        assertThat(response.nombres()).isEqualTo("Ana");
        assertThat(response.apellidos()).isEqualTo("Zambrano");
        assertThat(response.cedula()).isEqualTo("0102030405");
        assertThat(response.genero()).isEqualTo("Masculino");
        assertThat(response.programaActual()).isEqualTo("Aspirantes Policia");
        assertThat(response.pruebasFisicas().baterias()).isEmpty();
    }

    @Test
    void consultaPublicaPorCedulaRetornaNotFoundSiNoExiste() {
        when(alumnoRepository.findByCedulaConTodo("0102030405")).thenReturn(List.of());

        assertThatThrownBy(() -> service.obtenerMatrizFisicaPublicaPorCedula("0102030405"))
                .isInstanceOfSatisfying(ResponseStatusException.class, error ->
                        assertThat(error.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND));
    }

    @Test
    void consultaPublicaPorCedulaRetornaConflictSiCedulaEstaDuplicada() {
        Academia academiaUno = academia(1L);
        Academia academiaDos = academia(2L);
        Programa programaUno = programa(10L, academiaUno, "Programa Uno");
        Programa programaDos = programa(20L, academiaDos, "Programa Dos");
        Alumno alumnoUno = alumno(100L, academiaUno, programaUno, "Zambrano", "Ana");
        Alumno alumnoDos = alumno(200L, academiaDos, programaDos, "Zambrano", "Ana");

        when(alumnoRepository.findByCedulaConTodo("0102030405"))
                .thenReturn(List.of(alumnoUno, alumnoDos));

        assertThatThrownBy(() -> service.obtenerMatrizFisicaPublicaPorCedula("0102030405"))
                .isInstanceOfSatisfying(ResponseStatusException.class, error ->
                        assertThat(error.getStatusCode()).isEqualTo(HttpStatus.CONFLICT));
    }

    private Academia academia(Long id) {
        Academia academia = new Academia();
        academia.setId(id);
        academia.setNombre("Academia");
        return academia;
    }

    private Programa programa(Long id, Academia academia, String nombre) {
        Programa programa = new Programa();
        programa.setId(id);
        programa.setAcademia(academia);
        programa.setNombre(nombre);
        return programa;
    }

    private Alumno alumno(Long id, Academia academia, Programa programa, String apellidos, String nombres) {
        Alumno alumno = new Alumno();
        alumno.setId(id);
        alumno.setAcademia(academia);
        alumno.setProgramaActual(programa);
        alumno.setApellidos(apellidos);
        alumno.setNombres(nombres);
        alumno.setCedula(String.valueOf(id));
        alumno.setGenero(Genero.M);
        return alumno;
    }

    private JornadaFisica jornada(Long id, Alumno alumno, Academia academia, Programa programa, LocalDate fecha) {
        JornadaFisica jornada = new JornadaFisica();
        jornada.setId(id);
        jornada.setAlumno(alumno);
        jornada.setAcademia(academia);
        jornada.setPrograma(programa);
        jornada.setFecha(fecha);
        return jornada;
    }

    private ProgramaPruebaFisica prueba(Long id, Programa programa, String nombre, TipoValorPrueba tipo,
                                        OperadorObjetivo operador, String objetivo) {
        ProgramaPruebaFisica prueba = new ProgramaPruebaFisica();
        prueba.setId(id);
        prueba.setPrograma(programa);
        prueba.setNombre(nombre);
        prueba.setTipoValor(tipo);
        prueba.setOperador(operador);
        prueba.setObjetivoValor(new BigDecimal(objetivo));
        prueba.setActivo(true);
        return prueba;
    }

    private ResultadoFisico resultado(JornadaFisica jornada, ProgramaPruebaFisica prueba, String valor) {
        ResultadoFisico resultado = new ResultadoFisico();
        resultado.setJornadaFisica(jornada);
        resultado.setProgramaPruebaFisica(prueba);
        resultado.setValorNum(new BigDecimal(valor));
        return resultado;
    }
}

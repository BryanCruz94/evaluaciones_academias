package com.nairbdev.academiasbackend.controller;

import com.nairbdev.academiasbackend.dto.alumnos.AlumnoInfoDto;
import com.nairbdev.academiasbackend.dto.alumnos.AlumnoPruebaFisicaDto;
import com.nairbdev.academiasbackend.dto.alumnos.AlumnoUpdateDto;
import com.nairbdev.academiasbackend.dto.alumnos.AlumnosListDto;
import com.nairbdev.academiasbackend.dto.jornadaFisica.JornadaFisicaCreateRequest;
import com.nairbdev.academiasbackend.dto.jornadaFisica.JornadaFisicaResponseDTO;
import com.nairbdev.academiasbackend.entity.EstadoAlumno;
import com.nairbdev.academiasbackend.entity.Genero;
import com.nairbdev.academiasbackend.service.AlumnoService;
import com.nairbdev.academiasbackend.service.JornadaFisicaService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api-registro/alumnos")
@Tag(name = "Alumnos", description = "Gestión de alumnos, estados, fotos y jornadas físicas")
public class AlumnoController {

    private final AlumnoService service;
    private final JornadaFisicaService jornadaFisicaService;

    public AlumnoController(AlumnoService service, JornadaFisicaService jornadaFisicaService) {
        this.service = service;
        this.jornadaFisicaService = jornadaFisicaService;
    }

    @Operation(
            summary = "Listar todos los alumnos",
            description = "Devuelve todos los alumnos registrados (uso administrativo/reportes)"
    )
    @GetMapping
    public List<AlumnosListDto> todos() {
        return service.obtenerTodos();
    }

    @Operation(
            summary = "Listar alumnos activos por academia",
            description = "Devuelve solo los alumnos con estado ACTIVO de una academia específica"
    )
    @GetMapping("/activos/academia/{academiaId}")
    public List<AlumnosListDto> activos(@PathVariable Long academiaId) {
        return service.obtenerActivosPorAcademia(academiaId);
    }

    @Operation(
            summary = "Obtener alumno por ID",
            description = "Devuelve la información completa de un alumno por su ID"
    )
    @GetMapping("/{id}")
    public AlumnoInfoDto porId(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }

    @Operation(
            summary = "Obtener alumno por cédula",
            description = "Busca un alumno por cédula dentro de una academia"
    )
    @GetMapping("/academia/{academiaId}/cedula/{cedula}")
    public AlumnoInfoDto porCedula(@PathVariable Long academiaId, @PathVariable String cedula) {
        return service.obtenerPorCedula(academiaId, cedula.trim());
    }

    @Operation(
            summary = "Crear alumno con foto",
            description = "Crea un alumno en una academia. Recibe form-data y sube la foto a Cloudinary."
    )
    @PostMapping("/academia/{academiaId}")
    public AlumnoInfoDto crearAlumno(
            @PathVariable Long academiaId,
            @RequestParam("apellidos") String apellidos,
            @RequestParam("nombres") String nombres,
            @RequestParam("cedula") String cedula,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "fechaNacimiento", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaNacimiento,
            @RequestParam("genero") Genero genero,
            @RequestParam(value = "fechaIngreso", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaIngreso,
            @RequestParam(value = "fechaSalida", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaSalida,
            @RequestParam(value = "estaturaCm", required = false) Double estaturaCm,
            @RequestParam(value = "pesoKg", required = false) Double pesoKg,
            @RequestParam(value = "programaActualId", required = false) Long programaActualId,
            @RequestParam("foto") MultipartFile foto
    ) throws IOException {
        return service.crearConFoto(
                academiaId,
                apellidos, nombres, cedula, email,
                fechaNacimiento, genero,
                fechaIngreso, fechaSalida,
                estaturaCm, pesoKg,
                programaActualId,
                foto
        );
    }

    @Operation(
            summary = "Editar datos informativos",
            description = "Actualiza datos del alumno (no cambia academia, estado ni cédula)"
    )
    @PutMapping("/{id}")
    public AlumnoInfoDto editar(@PathVariable Long id, @RequestBody AlumnoUpdateDto dto) {
        return service.editarDatosInformativos(id, dto);
    }

    @Operation(
            summary = "Cambiar estado (Entrenador)",
            description = "Permite cambiar el estado del alumno solo a BAJA o GRADUADO"
    )
    @PatchMapping("/{id}/estado")
    public AlumnoInfoDto estadoAlumnoPorEntrenador(
            @PathVariable Long id,
            @RequestParam EstadoAlumno estado
    ) {
        return service.cambiarEstadoEntrenador(id, estado);
    }

    @Operation(
            summary = "Cambiar estado (Admin)",
            description = "Permite cambiar el estado del alumno a cualquier valor"
    )
    @PatchMapping("/{id}/estado/admin")
    public AlumnoInfoDto estadoAdmin(
            @PathVariable Long id,
            @RequestParam EstadoAlumno estado
    ) {
        return service.cambiarEstadoAdmin(id, estado);
    }

    @Operation(
            summary = "Actualizar foto del alumno",
            description = "Actualiza únicamente la foto del alumno"
    )
    @PatchMapping("/{id}/foto")
    public AlumnoInfoDto actualizarFoto(
            @PathVariable Long id,
            @RequestParam("foto") MultipartFile foto
    ) throws IOException {
        return service.actualizarFoto(id, foto);
    }

    @Operation(
            summary = "Listar pruebas físicas del alumno",
            description = "Obtiene las pruebas físicas asignadas según el programa actual del alumno"
    )
    @GetMapping("/academia/{academiaId}/{alumnoId}/pruebas-fisicas")
    public List<AlumnoPruebaFisicaDto> pruebasFisicasPorAlumno(
            @PathVariable Long academiaId,
            @PathVariable Long alumnoId
    ) {
        return service.obtenerPruebasFisicasPorAlumno(academiaId, alumnoId);
    }

    @Operation(
            summary = "Registrar jornada física",
            description = "Registra una jornada física con los resultados de pruebas del alumno"
    )
    @PostMapping("/academia/{academiaId}/{alumnoId}/jornadas-fisicas/evaluador/{evaluadorId}")
    public JornadaFisicaResponseDTO registrarJornadaFisica(
            @PathVariable Long academiaId,
            @PathVariable Long alumnoId,
            @PathVariable Long evaluadorId,
            @RequestBody JornadaFisicaCreateRequest request
    ) {
        return jornadaFisicaService.registrarJornadaFisica(
                academiaId, alumnoId, evaluadorId, request
        );
    }
}

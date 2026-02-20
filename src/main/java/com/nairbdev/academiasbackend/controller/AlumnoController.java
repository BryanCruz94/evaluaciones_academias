package com.nairbdev.academiasbackend.controller;

import com.nairbdev.academiasbackend.dto.alumnos.AlumnoInfoDto;
import com.nairbdev.academiasbackend.dto.alumnos.AlumnoUpdateDto;
import com.nairbdev.academiasbackend.dto.alumnos.AlumnosListDto;
import com.nairbdev.academiasbackend.entity.EstadoAlumno;
import com.nairbdev.academiasbackend.entity.Genero;
import com.nairbdev.academiasbackend.service.AlumnoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api-registro/alumnos")
public class AlumnoController {

    private final AlumnoService service;

    public AlumnoController(AlumnoService service) {
        this.service = service;
    }

    // Lista todos los alumnos (útil para admin/reportes)
    @GetMapping
    public List<AlumnosListDto> todos() {
        return service.obtenerTodos();
    }

    // Lista alumnos ACTIVO filtrados por academia
    @GetMapping("/activos/academia/{academiaId}")
    public List<AlumnosListDto> activos(@PathVariable Long academiaId) {
        return service.obtenerActivosPorAcademia(academiaId);
    }

    // Obtiene el alumno por ID (para pantalla de edición/consulta)
    @GetMapping("/{id}")
    public AlumnoInfoDto porId(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }

    // Obtiene alumno por cédula dentro de una academia
    @GetMapping("/academia/{academiaId}/cedula/{cedula}")
    public AlumnoInfoDto porCedula(@PathVariable Long academiaId, @PathVariable String cedula) {
        return service.obtenerPorCedula(academiaId, cedula.trim());
    }

    /**
     * Crea un alumno en una academia (academiaId va en la URL).
     * Recibe los campos como form-data (RequestParam) + foto (MultipartFile).
     * Sube la foto a Cloudinary y guarda el secure_url en BD.
     */
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

    // Edita datos informativos (NO cambia academia, estado, cédula). Devuelve AlumnoInfoDto actualizado.
    @PutMapping("/{id}")
    public AlumnoInfoDto editar(@PathVariable Long id, @RequestBody AlumnoUpdateDto dto) {
        return service.editarDatosInformativos(id, dto);
    }

    // Entrenador: solo BAJA o GRADUADO
    @PatchMapping("/{id}/estado")
    public AlumnoInfoDto estadoAlumnoPorEntrenador(@PathVariable Long id, @RequestParam EstadoAlumno estado) {
        return service.cambiarEstadoEntrenador(id, estado);
    }

    // Admin: cualquier estado
    @PatchMapping("/{id}/estado/admin")
    public AlumnoInfoDto estadoAdmin(@PathVariable Long id, @RequestParam EstadoAlumno estado) {
        return service.cambiarEstadoAdmin(id, estado);
    }

    /**
     * Actualiza SOLO la foto del alumno (recomendado para mantener simple el PUT).
     * Recibe form-data con "foto".
     */
    @PatchMapping("/{id}/foto")
    public AlumnoInfoDto actualizarFoto(@PathVariable Long id, @RequestParam("foto") MultipartFile foto) throws IOException {
        return service.actualizarFoto(id, foto);
    }
}

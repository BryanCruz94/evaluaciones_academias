package com.nairbdev.academiasbackend.service;

import com.nairbdev.academiasbackend.dto.alumnos.AlumnoInfoDto;
import com.nairbdev.academiasbackend.dto.alumnos.AlumnoPruebaFisicaDto;
import com.nairbdev.academiasbackend.dto.alumnos.AlumnoUpdateDto;
import com.nairbdev.academiasbackend.dto.alumnos.AlumnosListDto;
import com.nairbdev.academiasbackend.entity.*;
import com.nairbdev.academiasbackend.repository.AcademiaRepository;
import com.nairbdev.academiasbackend.repository.AlumnoRepository;
import com.nairbdev.academiasbackend.repository.ProgramaPruebaFisicaRepository;
import com.nairbdev.academiasbackend.repository.ProgramaRepository;
import com.nairbdev.academiasbackend.utils.ImageUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
@Transactional
public class AlumnoService {

    private final AlumnoRepository alumnoRepo;
    private final AcademiaRepository academiaRepo;
    private final ProgramaRepository programaRepo;
    private final CloudinaryService cloudinaryService;
    private final ProgramaPruebaFisicaRepository programaPruebaFisicaRepository;

    public AlumnoService(AlumnoRepository alumnoRepo,
                         AcademiaRepository academiaRepo,
                         ProgramaRepository programaRepo,
                         CloudinaryService cloudinaryService,
                         ProgramaPruebaFisicaRepository programaPruebaFisicaRepository) {
        this.alumnoRepo = alumnoRepo;
        this.academiaRepo = academiaRepo;
        this.programaRepo = programaRepo;
        this.cloudinaryService = cloudinaryService;
        this.programaPruebaFisicaRepository = programaPruebaFisicaRepository;
    }

    // Devuelve lista general de alumnos (para tablas/listados)
    @Transactional(readOnly = true)
    public List<AlumnosListDto> obtenerTodos() {
        return alumnoRepo.findAllConAcademia().stream().map(this::toDtoList).toList();
    }

    // Devuelve lista de alumnos ACTIVO por academia (para tablas/listados)
    @Transactional(readOnly = true)
    public List<AlumnosListDto> obtenerActivosPorAcademia(Long academiaId) {
        // IMPORTANTE: ideal que el repo use estado como parámetro enum (no 'ACTIVO' string)
        return alumnoRepo.findPorAcademiaYEstado(academiaId, EstadoAlumno.ACTIVO)
                .stream().map(this::toDtoList).toList();
    }

    // Obtiene un alumno por ID (para editar/ver detalles)
    @Transactional(readOnly = true)
    public AlumnoInfoDto obtenerPorId(Long id) {
        Alumno a = alumnoRepo.findByIdConTodo(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Alumno no encontrado: " + id));
        return toDto(a);
    }

    // Obtiene alumno por academia + cédula (cédula no es única global)
    @Transactional(readOnly = true)
    public AlumnoInfoDto obtenerPorCedula(Long academiaId, String cedula) {
        Alumno a = alumnoRepo.findByAcademiaIdAndCedula(academiaId, cedula)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Alumno no encontrado con cédula " + cedula + " en academia " + academiaId));
        return toDto(a);
    }

    /**
     * Crea alumno + sube foto a Cloudinary.
     * Guarda en BD el secure_url como fotoUrl.
     */
    public AlumnoInfoDto crearConFoto(
            Long academiaId,
            String apellidos,
            String nombres,
            String cedula,
            String email,
            LocalDate fechaNacimiento,
            Genero genero,
            LocalDate fechaIngreso,
            LocalDate fechaSalida,
            Double estaturaCm,
            Double pesoKg,
            Long programaActualId,
            MultipartFile foto
    ) throws IOException {
        if (cedula == null || cedula.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cédula es obligatoria.");
        }
        String cedulaTrim = cedula.trim();

        if (alumnoRepo.existsByAcademiaIdAndCedula(academiaId, cedulaTrim)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un alumno con esa cédula en esta academia.");
        }

        Academia academia = academiaRepo.findById(academiaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Academia no encontrada: " + academiaId));

        Programa programa = null;
        if (programaActualId != null) {
            programa = programaRepo.findById(programaActualId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Programa no encontrado: " + programaActualId));
        }
        String fotoUrl = null;
        if(foto == null || foto.isEmpty()){
            fotoUrl = "https://res.cloudinary.com/dadjbbua7/image/upload/v1771557393/logoFoto_f5mmlx.jpg";
        }else{
            byte[] imagenRedimensionada = ImageUtils.redimensionar(foto, 900, 1110);

            fotoUrl = cloudinaryService.uploadAlumnoPhoto(
                    imagenRedimensionada,
                    cedulaTrim
            );
        }


        Alumno a = new Alumno();
        a.setAcademia(academia);
        a.setEstado(EstadoAlumno.ACTIVO);
        a.setCedula(cedulaTrim);
        a.setApellidos(apellidos);
        a.setNombres(nombres);
        a.setEmail(email);
        a.setFechaNacimiento(fechaNacimiento);
        a.setGenero(genero);
        a.setFechaIngreso(fechaIngreso);
        a.setFechaSalida(fechaSalida);
        a.setEstaturaCm(estaturaCm);
        a.setPesoKg(pesoKg);
        a.setProgramaActual(programa);
        a.setFotoUrl(fotoUrl);

        Alumno saved = alumnoRepo.save(a);
        return toDto(saved);
    }

    // Edita datos informativos (sin estado, sin academia, sin cédula)
    public AlumnoInfoDto editarDatosInformativos(Long id, AlumnoUpdateDto dto) {
        Alumno a = alumnoRepo.findByIdConTodo(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Alumno no encontrado: " + id));

        Programa programa = null;
        if (dto.programaActualId() != null) {
            programa = programaRepo.findById(dto.programaActualId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Programa no encontrado: " + dto.programaActualId()));
        }

        a.setApellidos(dto.apellidos());
        a.setNombres(dto.nombres());
        a.setEmail(dto.email());
        a.setFechaNacimiento(dto.fechaNacimiento());
        a.setGenero(dto.genero());
        a.setFechaIngreso(dto.fechaIngreso());
        a.setFechaSalida(dto.fechaSalida());
        a.setEstaturaCm(dto.estaturaCm());
        a.setPesoKg(dto.pesoKg());
        a.setProgramaActual(programa);
        a.setCedula(dto.cedula());

        Alumno saved = alumnoRepo.save(a);
        return toDto(saved);
    }

    // Cambia estado para entrenador: solo BAJA o GRADUADO
    public AlumnoInfoDto cambiarEstadoEntrenador(Long id, EstadoAlumno estado) {
        if (!(estado == EstadoAlumno.BAJA || estado == EstadoAlumno.GRADUADO)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entrenador solo puede: BAJA o GRADUADO");
        }
        return cambiarEstado(id, estado);
    }

    // Cambia estado para admin: cualquier EstadoAlumno
    public AlumnoInfoDto cambiarEstadoAdmin(Long id, EstadoAlumno estado) {
        return cambiarEstado(id, estado);
    }

    // Actualiza únicamente la foto del alumno (sube a Cloudinary y guarda fotoUrl)
    public AlumnoInfoDto actualizarFoto(Long id, MultipartFile foto) throws IOException {
        Alumno a = alumnoRepo.findByIdConTodo(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Alumno no encontrado: " + id));

        byte[] imagenRedimensionada = ImageUtils.redimensionar(foto, 900, 1110);

        String fotoUrl = cloudinaryService.uploadAlumnoPhoto(
                imagenRedimensionada,
                a.getCedula()
        );
        a.setFotoUrl(fotoUrl);

        return toDto(a);
    }

    private AlumnoInfoDto cambiarEstado(Long id, EstadoAlumno estado) {
        Alumno a = alumnoRepo.findByIdConTodo(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Alumno no encontrado: " + id));
        a.setEstado(estado);
        return toDto(a);
    }

    // Mapper informativo con IMC redondeado a 2 decimales
    private AlumnoInfoDto toDto(Alumno a) {
        Integer edad = (a.getFechaNacimiento() == null) ? null :
                Period.between(a.getFechaNacimiento(), LocalDate.now()).getYears();

        Double imc = null;
        if (a.getPesoKg() != null && a.getEstaturaCm() != null && a.getEstaturaCm() > 0) {
            double estaturaM = a.getEstaturaCm() / 100.0;
            double imcCalc = a.getPesoKg() / (estaturaM * estaturaM);
            imc = BigDecimal.valueOf(imcCalc).setScale(2, RoundingMode.HALF_UP).doubleValue();
        }

        String generoTexto = (a.getGenero() == null) ? null : (a.getGenero() == Genero.M ? "Masculino" : "Femenino");
        String programaNombre = (a.getProgramaActual() == null) ? null : a.getProgramaActual().getNombre();

        return new AlumnoInfoDto(
                a.getId(),
                a.getAcademia().getNombre(),
                a.getApellidos(),
                a.getNombres(),
                a.getCedula(),
                a.getEmail(),
                a.getFechaNacimiento(),
                edad,
                generoTexto,
                a.getFechaIngreso(),
                a.getFechaSalida(),
                a.getFotoUrl(),
                a.getEstaturaCm(),
                a.getPesoKg(),
                imc,
                programaNombre,
                a.getEstado()
        );
    }

    //Metodo para mostrar las pruebas físicas que debe dar un alumno por su programa
    public List<AlumnoPruebaFisicaDto> obtenerPruebasFisicasPorAlumno(Long academiaId, Long alumnoId) {

        Alumno alumno = alumnoRepo.findByIdConTodo(alumnoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Alumno no encontrado."));

        // Validar que el alumno pertenezca a la academia del URL
        if (!alumno.getAcademia().getId().equals(academiaId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Alumno no pertenece a la academia indicada.");
        }

        Programa programa = alumno.getProgramaActual();
        if (programa == null) {
            return null; // como pediste
        }

        // (Recomendado) validar coherencia: el programa del alumno también debe ser de la academia
        if (!programa.getAcademia().getId().equals(academiaId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "El programa actual del alumno no pertenece a la academia indicada.");
        }

        return programaPruebaFisicaRepository
                .findByProgramaIdAndActivoTrueOrderByIdAsc(programa.getId())
                .stream()
                .map(this::toDto)
                .toList();
    }

    private AlumnoPruebaFisicaDto toDto(ProgramaPruebaFisica e) {
        AlumnoPruebaFisicaDto dto = new AlumnoPruebaFisicaDto();
        dto.setId(e.getId());
        dto.setNombre(e.getNombre());
        dto.setDescripcion(e.getDescripcion());
        dto.setTipoValor(e.getTipoValor());
        dto.setUnidad(e.getUnidad());
        dto.setOperador(e.getOperador());
        dto.setObjetivoValor(e.getObjetivoValor());
        return dto;
    }

    private AlumnosListDto toDtoList(Alumno a) {
        String generoTexto = (a.getGenero() == null) ? null : (a.getGenero() == Genero.M ? "Masculino" : "Femenino");
        String programaNombre = (a.getProgramaActual() == null) ? null : a.getProgramaActual().getNombre();

        return new AlumnosListDto(
                a.getId(),
                a.getAcademia().getNombre(),
                a.getApellidos(),
                a.getNombres(),
                a.getCedula(),
                a.getEmail(),
                generoTexto,
                a.getFechaIngreso(),
                a.getFechaSalida(),
                programaNombre,
                a.getEstado()
        );
    }
}

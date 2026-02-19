package com.nairbdev.academiasbackend.service;

import com.nairbdev.academiasbackend.dto.AlumnoInfoDto;
import com.nairbdev.academiasbackend.dto.AlumnosListDto;
import com.nairbdev.academiasbackend.entity.Alumno;
import com.nairbdev.academiasbackend.entity.Genero;
import com.nairbdev.academiasbackend.repository.AlumnoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class AlumnoService {

    private final AlumnoRepository repo;

    public AlumnoService(AlumnoRepository repo) {
        this.repo = repo;
    }

    public List<AlumnosListDto> obtenerTodos() {
        return repo.findAllConAcademia().stream()
                .map(this::toDtoList)
                .toList();
    }

    public List<AlumnosListDto> obtenerActivosPorAcademia(Long academiaId) {
        return repo.findActivosPorAcademia(academiaId).stream()
                .map(this::toDtoList)
                .toList();
    }

    public AlumnoInfoDto obtenerPorCedula(String cedula) {
        Alumno a = repo.findByCedulaConAcademia(cedula)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Alumno no encontrado con cédula: " + cedula
                ));
        return toDto(a);
    }

    private AlumnoInfoDto toDto(Alumno a) {
        Double imc = null;
        Integer edad = (a.getFechaNacimiento() == null) ? null :
                Period.between(a.getFechaNacimiento(), LocalDate.now()).getYears();

        if (a.getPesoKg() != null && a.getEstaturaCm() != null && a.getEstaturaCm() > 0) {
            double estaturaM = a.getEstaturaCm() / 100.0;
            double imcCalculado = a.getPesoKg() / (estaturaM * estaturaM);

            imc = BigDecimal
                    .valueOf(imcCalculado)
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();
        }

        String generoTexto = (a.getGenero() == null) ? null :
                (a.getGenero() == Genero.M ? "Masculino" : "Femenino");

        String programaNombre = (a.getProgramaActual() == null) ? null : a.getProgramaActual().getNombre();

        return new AlumnoInfoDto(
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
    private AlumnosListDto toDtoList(Alumno a){
        String generoTexto = (a.getGenero() == null) ? null :
                (a.getGenero() == Genero.M ? "Masculino" : "Femenino");

        String programaNombre = (a.getProgramaActual() == null) ? null : a.getProgramaActual().getNombre();
        return new AlumnosListDto(
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
package com.nairbdev.academiasbackend.controller;

import com.nairbdev.academiasbackend.dto.AlumnoInfoDto;
import com.nairbdev.academiasbackend.dto.AlumnosListDto;
import com.nairbdev.academiasbackend.service.AlumnoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api-registro/alumnos")
public class AlumnoController {

    private final AlumnoService service;

    public AlumnoController(AlumnoService service) {
        this.service = service;
    }

    @GetMapping
    public List<AlumnosListDto> todos() {
        return service.obtenerTodos();
    }

    @GetMapping("/activos/academia/{academiaId}")
    public List<AlumnosListDto> activos(@PathVariable Long academiaId) {
        return service.obtenerActivosPorAcademia(academiaId);
    }

    @GetMapping("/cedula/{cedula}")
    public AlumnoInfoDto porCedula(@PathVariable String cedula) {
        return service.obtenerPorCedula(cedula.trim());
    }
}

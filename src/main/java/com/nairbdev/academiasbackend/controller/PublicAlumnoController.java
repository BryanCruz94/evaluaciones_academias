package com.nairbdev.academiasbackend.controller;

import com.nairbdev.academiasbackend.dto.pruebasFisicas.AlumnoPublicoPruebasFisicasDTO;
import com.nairbdev.academiasbackend.service.JornadaFisicaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/alumnos")
@Tag(name = "Consulta legacy", description = "Consulta autenticada de informacion fisica por cedula")
public class PublicAlumnoController {

    private final JornadaFisicaService jornadaFisicaService;

    public PublicAlumnoController(JornadaFisicaService jornadaFisicaService) {
        this.jornadaFisicaService = jornadaFisicaService;
    }

    @Operation(
            summary = "Consultar pruebas fisicas por cedula",
            description = "Endpoint legado autenticado. La pantalla principal usa /api-registro/verificar-pruebas-fisicas."
    )
    @GetMapping("/cedula/{cedula}/pruebas-fisicas")
    public AlumnoPublicoPruebasFisicasDTO obtenerPruebasFisicasPorCedula(
            @PathVariable String cedula,
            @AuthenticationPrincipal Jwt jwt,
            @RequestHeader(value = "X-User-Name", required = false) String nombreUsuario
    ) {
        return jornadaFisicaService.obtenerMatrizFisicaAuditadaPorCedula(
                cedula,
                obtenerNombreConsultor(jwt, nombreUsuario)
        );
    }

    private String obtenerNombreConsultor(Jwt jwt, String nombreUsuario) {
        if (jwt == null) return null;

        String name = jwt.getClaimAsString("name");
        if (name != null && !name.isBlank()) return name;

        String nickname = jwt.getClaimAsString("nickname");
        if (nickname != null && !nickname.isBlank()) return nickname;

        String email = jwt.getClaimAsString("email");
        if (email != null && !email.isBlank()) return email;

        if (nombreUsuario != null && !nombreUsuario.isBlank()) {
            return nombreUsuario;
        }

        return jwt.getSubject();
    }
}

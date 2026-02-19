package com.nairbdev.academiasbackend.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(
        name = "alumno",
        uniqueConstraints = @UniqueConstraint(name = "uq_alumno_email", columnNames = {"academia_id", "email"}),
        indexes = @Index(name = "idx_alumno_academia", columnList = "academia_id")
)
public class Alumno extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "academia_id", nullable = false)
    private Academia academia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "programa_actual_id")
    private Programa programaActual;

    @Column(nullable = false, length = 150)
    private String nombres;

    @Column(nullable = false, length = 150)
    private String apellidos;

    @Column(length = 254)
    private String email;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "genero")
    private Genero genero;

    @Column(name = "estatura_cm", precision = 4, scale = 2)
    private BigDecimal estaturaCm;

    @Column(name = "peso_kg", precision = 4, scale = 2)
    private BigDecimal pesoKg;

    @Column(precision = 5, scale = 2)
    private BigDecimal imc;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "estado_alumno")
    private EstadoAlumno estado = EstadoAlumno.ACTIVO;

    @Column(name = "fecha_ingreso")
    private LocalDate fechaIngreso;

    @Column(name = "fecha_salida")
    private LocalDate fechaSalida;

    @Column(name = "foto_url", columnDefinition = "text")
    private String fotoUrl;

    public Long getId() { return id; }
    public Academia getAcademia() { return academia; }
    public Programa getProgramaActual() { return programaActual; }
    public String getNombres() { return nombres; }
    public String getApellidos() { return apellidos; }
    public String getEmail() { return email; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public Genero getGenero() { return genero; }
    public BigDecimal getEstaturaCm() { return estaturaCm; }
    public BigDecimal getPesoKg() { return pesoKg; }
    public BigDecimal getImc() { return imc; }
    public EstadoAlumno getEstado() { return estado; }
    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public LocalDate getFechaSalida() { return fechaSalida; }
    public String getFotoUrl() { return fotoUrl; }

    public void setId(Long id) { this.id = id; }
    public void setAcademia(Academia academia) { this.academia = academia; }
    public void setProgramaActual(Programa programaActual) { this.programaActual = programaActual; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public void setEmail(String email) { this.email = email; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public void setGenero(Genero genero) { this.genero = genero; }
    public void setEstaturaCm(BigDecimal estaturaCm) { this.estaturaCm = estaturaCm; }
    public void setPesoKg(BigDecimal pesoKg) { this.pesoKg = pesoKg; }
    public void setImc(BigDecimal imc) { this.imc = imc; }
    public void setEstado(EstadoAlumno estado) { this.estado = estado; }
    public void setFechaIngreso(LocalDate fechaIngreso) { this.fechaIngreso = fechaIngreso; }
    public void setFechaSalida(LocalDate fechaSalida) { this.fechaSalida = fechaSalida; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }
}

package com.nairbdev.academiasbackend.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(
        name = "jornada_fisica",
        uniqueConstraints = @UniqueConstraint(name = "uq_jornada_fisica", columnNames = {"alumno_id", "fecha"})
)
public class JornadaFisica extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "alumno_id", nullable = false)
    private Alumno alumno;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "academia_id", nullable = false)
    private Academia academia;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "programa_id", nullable = false)
    private Programa programa;

    @Column(nullable = false)
    private LocalDate fecha;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "evaluador_id", nullable = false)
    private Usuario evaluador;

    @Column(columnDefinition = "TEXT")
    private String observacion;

    @Column(name = "peso_kg", precision = 4, scale = 2)
    private BigDecimal pesoKg;

    @Column(precision = 5, scale = 2)
    private BigDecimal imc;

    public Long getId() { return id; }
    public Alumno getAlumno() { return alumno; }
    public Academia getAcademia() { return academia; }
    public Programa getPrograma() { return programa; }
    public LocalDate getFecha() { return fecha; }
    public Usuario getEvaluador() { return evaluador; }
    public String getObservacion() { return observacion; }
    public BigDecimal getPesoKg() { return pesoKg; }
    public BigDecimal getImc() { return imc; }

    public void setId(Long id) { this.id = id; }
    public void setAlumno(Alumno alumno) { this.alumno = alumno; }
    public void setAcademia(Academia academia) { this.academia = academia; }
    public void setPrograma(Programa programa) { this.programa = programa; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public void setEvaluador(Usuario evaluador) { this.evaluador = evaluador; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
    public void setPesoKg(BigDecimal pesoKg) { this.pesoKg = pesoKg; }
    public void setImc(BigDecimal imc) { this.imc = imc; }
}

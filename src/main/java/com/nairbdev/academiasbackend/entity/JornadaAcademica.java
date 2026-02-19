package com.nairbdev.academiasbackend.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(
        name = "jornada_academica",
        uniqueConstraints = @UniqueConstraint(name = "uq_jornada_academica", columnNames = {"alumno_id", "fecha"}),
        indexes = @Index(name = "idx_ja_programa_fecha", columnList = "programa_id,fecha")
)
public class JornadaAcademica extends AuditableEntity {

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

    @Column(columnDefinition = "text")
    private String observacion;

    public Long getId() { return id; }
    public Alumno getAlumno() { return alumno; }
    public Academia getAcademia() { return academia; }
    public Programa getPrograma() { return programa; }
    public LocalDate getFecha() { return fecha; }
    public Usuario getEvaluador() { return evaluador; }
    public String getObservacion() { return observacion; }

    public void setId(Long id) { this.id = id; }
    public void setAlumno(Alumno alumno) { this.alumno = alumno; }
    public void setAcademia(Academia academia) { this.academia = academia; }
    public void setPrograma(Programa programa) { this.programa = programa; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public void setEvaluador(Usuario evaluador) { this.evaluador = evaluador; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
}

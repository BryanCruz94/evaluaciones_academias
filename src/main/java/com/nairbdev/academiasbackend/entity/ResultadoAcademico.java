package com.nairbdev.academiasbackend.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "resultado_academico",
        uniqueConstraints = @UniqueConstraint(name = "uq_resultado_academico", columnNames = {"jornada_academica_id", "materia_id"}),
        indexes = @Index(name = "idx_ra_jornada", columnList = "jornada_academica_id")
)
public class ResultadoAcademico extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "jornada_academica_id", nullable = false)
    private JornadaAcademica jornadaAcademica;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "materia_id", nullable = false)
    private Materia materia;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal nota;

    @Column(columnDefinition = "text")
    private String observacion;

    public Long getId() { return id; }
    public JornadaAcademica getJornadaAcademica() { return jornadaAcademica; }
    public Materia getMateria() { return materia; }
    public BigDecimal getNota() { return nota; }
    public String getObservacion() { return observacion; }

    public void setId(Long id) { this.id = id; }
    public void setJornadaAcademica(JornadaAcademica jornadaAcademica) { this.jornadaAcademica = jornadaAcademica; }
    public void setMateria(Materia materia) { this.materia = materia; }
    public void setNota(BigDecimal nota) { this.nota = nota; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
}

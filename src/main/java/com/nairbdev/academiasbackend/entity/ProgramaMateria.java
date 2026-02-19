package com.nairbdev.academiasbackend.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "programa_materia",
        uniqueConstraints = @UniqueConstraint(name = "uq_programa_materia", columnNames = {"programa_id", "materia_id"})
)
public class ProgramaMateria extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "programa_id", nullable = false)
    private Programa programa;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "materia_id", nullable = false)
    private Materia materia;

    @Column(nullable = false)
    private Integer orden;

    @Column(name = "nota_objetivo", precision = 5, scale = 2)
    private BigDecimal notaObjetivo;

    @Column(nullable = false)
    private Boolean activo = true;

    public Long getId() { return id; }
    public Programa getPrograma() { return programa; }
    public Materia getMateria() { return materia; }
    public Integer getOrden() { return orden; }
    public BigDecimal getNotaObjetivo() { return notaObjetivo; }
    public Boolean getActivo() { return activo; }

    public void setId(Long id) { this.id = id; }
    public void setPrograma(Programa programa) { this.programa = programa; }
    public void setMateria(Materia materia) { this.materia = materia; }
    public void setOrden(Integer orden) { this.orden = orden; }
    public void setNotaObjetivo(BigDecimal notaObjetivo) { this.notaObjetivo = notaObjetivo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}

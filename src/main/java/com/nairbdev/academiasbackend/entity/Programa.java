package com.nairbdev.academiasbackend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(
        name = "programa",
        uniqueConstraints = @UniqueConstraint(name = "uq_programa_nombre", columnNames = {"academia_id", "nombre"}),
        indexes = @Index(name = "idx_programa_academia", columnList = "academia_id")
)
public class Programa extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "academia_id", nullable = false)
    private Academia academia;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(columnDefinition = "text")
    private String descripcion;

    @Column(nullable = false)
    private Boolean activo = true;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(columnDefinition = "genero")
    private Genero genero;

    public Long getId() { return id; }
    public Academia getAcademia() { return academia; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public Boolean getActivo() { return activo; }

    public void setId(Long id) { this.id = id; }
    public void setAcademia(Academia academia) { this.academia = academia; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public Genero getGenero(Genero genero) { return this.genero; }
    public void setGenero(Genero genero) { this.genero = genero; }
}

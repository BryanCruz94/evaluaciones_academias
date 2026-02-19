package com.nairbdev.academiasbackend.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "materia",
        uniqueConstraints = @UniqueConstraint(name = "uq_materia_nombre", columnNames = {"academia_id", "nombre"})
)
public class Materia extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "academia_id", nullable = false)
    private Academia academia;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false)
    private Boolean activo = true;

    public Long getId() { return id; }
    public Academia getAcademia() { return academia; }
    public String getNombre() { return nombre; }
    public Boolean getActivo() { return activo; }

    public void setId(Long id) { this.id = id; }
    public void setAcademia(Academia academia) { this.academia = academia; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}

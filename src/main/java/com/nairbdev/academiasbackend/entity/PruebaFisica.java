package com.nairbdev.academiasbackend.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "prueba_fisica",
        uniqueConstraints = @UniqueConstraint(name = "uq_prueba_fisica_nombre", columnNames = {"academia_id", "nombre"}),
        indexes = @Index(name = "idx_prueba_fisica_academia", columnList = "academia_id")
)
public class PruebaFisica extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "academia_id", nullable = false)
    private Academia academia;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_valor", nullable = false, columnDefinition = "tipo_valor_prueba")
    private TipoValorPrueba tipoValor;

    @Column(length = 30)
    private String unidad;

    @Column(nullable = false)
    private Boolean activo = true;

    public Long getId() { return id; }
    public Academia getAcademia() { return academia; }
    public String getNombre() { return nombre; }
    public TipoValorPrueba getTipoValor() { return tipoValor; }
    public String getUnidad() { return unidad; }
    public Boolean getActivo() { return activo; }

    public void setId(Long id) { this.id = id; }
    public void setAcademia(Academia academia) { this.academia = academia; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setTipoValor(TipoValorPrueba tipoValor) { this.tipoValor = tipoValor; }
    public void setUnidad(String unidad) { this.unidad = unidad; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}

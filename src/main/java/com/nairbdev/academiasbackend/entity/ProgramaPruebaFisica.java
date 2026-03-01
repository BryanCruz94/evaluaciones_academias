package com.nairbdev.academiasbackend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;

@Entity
@Table(
        name = "programa_prueba_fisica",
        indexes = @Index(name = "idx_ppf_programa", columnList = "programa_id")
)
public class ProgramaPruebaFisica extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "programa_id", nullable = false)
    private Programa programa;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "tipo_valor", nullable = false, columnDefinition = "tipo_valor_prueba")
    private TipoValorPrueba tipoValor;

    @Column(length = 30)
    private String unidad;

    @Column(name = "objetivo_valor", precision = 12, scale = 2)
    private BigDecimal objetivoValor;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "operador", columnDefinition = "operador_objetivo")
    private OperadorObjetivo operador;

    @Column(length = 80)
    private String etiqueta;

    @Column(nullable = false)
    private Boolean activo = true;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Programa getPrograma() {
        return programa;
    }

    public void setPrograma(Programa programa) {
        this.programa = programa;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TipoValorPrueba getTipoValor() {
        return tipoValor;
    }

    public void setTipoValor(TipoValorPrueba tipoValor) {
        this.tipoValor = tipoValor;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public BigDecimal getObjetivoValor() {
        return objetivoValor;
    }

    public void setObjetivoValor(BigDecimal objetivoValor) {
        this.objetivoValor = objetivoValor;
    }

    public OperadorObjetivo getOperador() {
        return operador;
    }

    public void setOperador(OperadorObjetivo operador) {
        this.operador = operador;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    // getters/setters...
}

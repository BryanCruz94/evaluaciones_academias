package com.nairbdev.academiasbackend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;

@Entity
@Table(
        name = "programa_prueba_fisica",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_programa_prueba_genero",
                columnNames = {"programa_id", "prueba_fisica_id", "genero"}
        ),
        indexes = @Index(name = "idx_ppf_programa", columnList = "programa_id")
)
public class ProgramaPruebaFisica extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "programa_id", nullable = false)
    private Programa programa;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "prueba_fisica_id", nullable = false)
    private PruebaFisica pruebaFisica;

    @Column(nullable = false)
    private Integer orden;

    @Column(name = "objetivo_valor", precision = 12, scale = 2)
    private BigDecimal objetivoValor;

    @Convert(converter = OperadorObjetivoConverter.class)
    @Column(name = "operador", columnDefinition = "operador_objetivo")
    private OperadorObjetivo operador;

    @Column(length = 80)
    private String etiqueta;


    @Column(nullable = false)
    private Boolean activo = true;

    public Long getId() { return id; }
    public Programa getPrograma() { return programa; }
    public PruebaFisica getPruebaFisica() { return pruebaFisica; }
    public Integer getOrden() { return orden; }
    public BigDecimal getObjetivoValor() { return objetivoValor; }
    public OperadorObjetivo getOperador() { return operador; }
    public String getEtiqueta() { return etiqueta; }
    public Boolean getActivo() { return activo; }

    public void setId(Long id) { this.id = id; }
    public void setPrograma(Programa programa) { this.programa = programa; }
    public void setPruebaFisica(PruebaFisica pruebaFisica) { this.pruebaFisica = pruebaFisica; }
    public void setOrden(Integer orden) { this.orden = orden; }
    public void setObjetivoValor(BigDecimal objetivoValor) { this.objetivoValor = objetivoValor; }
    public void setOperador(OperadorObjetivo operador) { this.operador = operador; }
    public void setEtiqueta(String etiqueta) { this.etiqueta = etiqueta; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}

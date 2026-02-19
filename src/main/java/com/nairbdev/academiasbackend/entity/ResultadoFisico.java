package com.nairbdev.academiasbackend.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "resultado_fisico",
        uniqueConstraints = @UniqueConstraint(name = "uq_resultado_fisico", columnNames = {"jornada_fisica_id", "prueba_fisica_id"}),
        indexes = @Index(name = "idx_rf_jornada", columnList = "jornada_fisica_id")
)
public class ResultadoFisico extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "jornada_fisica_id", nullable = false)
    private JornadaFisica jornadaFisica;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "prueba_fisica_id", nullable = false)
    private PruebaFisica pruebaFisica;

    @Column(name = "valor_num", precision = 12, scale = 2)
    private BigDecimal valorNum;

    @Column(name = "valor_bool")
    private Boolean valorBool;

    @Column(columnDefinition = "text")
    private String observacion;

    public Long getId() { return id; }
    public JornadaFisica getJornadaFisica() { return jornadaFisica; }
    public PruebaFisica getPruebaFisica() { return pruebaFisica; }
    public BigDecimal getValorNum() { return valorNum; }
    public Boolean getValorBool() { return valorBool; }
    public String getObservacion() { return observacion; }

    public void setId(Long id) { this.id = id; }
    public void setJornadaFisica(JornadaFisica jornadaFisica) { this.jornadaFisica = jornadaFisica; }
    public void setPruebaFisica(PruebaFisica pruebaFisica) { this.pruebaFisica = pruebaFisica; }
    public void setValorNum(BigDecimal valorNum) { this.valorNum = valorNum; }
    public void setValorBool(Boolean valorBool) { this.valorBool = valorBool; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
}

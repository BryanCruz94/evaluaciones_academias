package com.nairbdev.academiasbackend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(
        name = "resultado_fisico",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_resultado_fisico",
                columnNames = {"jornada_fisica_id", "programa_prueba_fisica_id"}
        )
)
public class ResultadoFisico extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "jornada_fisica_id", nullable = false)
    private JornadaFisica jornadaFisica;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "programa_prueba_fisica_id", nullable = false)
    private ProgramaPruebaFisica programaPruebaFisica;

    @Column(name = "valor_num", precision = 12, scale = 2)
    private BigDecimal valorNum;

    @Column(name = "valor_bool")
    private Boolean valorBool;

    @Column(columnDefinition = "TEXT")
    private String observacion;

    public Long getId() { return id; }
    public JornadaFisica getJornadaFisica() { return jornadaFisica; }
    public ProgramaPruebaFisica getProgramaPruebaFisica() { return programaPruebaFisica; }
    public BigDecimal getValorNum() { return valorNum; }
    public Boolean getValorBool() { return valorBool; }
    public String getObservacion() { return observacion; }

    public void setId(Long id) { this.id = id; }
    public void setJornadaFisica(JornadaFisica jornadaFisica) { this.jornadaFisica = jornadaFisica; }
    public void setProgramaPruebaFisica(ProgramaPruebaFisica ppf) { this.programaPruebaFisica = ppf; }
    public void setValorNum(BigDecimal valorNum) { this.valorNum = valorNum; }
    public void setValorBool(Boolean valorBool) { this.valorBool = valorBool; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
}
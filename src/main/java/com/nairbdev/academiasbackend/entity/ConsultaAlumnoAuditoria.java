package com.nairbdev.academiasbackend.entity;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(
        name = "consulta_alumno_auditoria",
        indexes = {
                @Index(name = "idx_consulta_alumno_fecha", columnList = "fecha_consulta"),
                @Index(name = "idx_consulta_alumno_cedula", columnList = "cedula_consultada")
        }
)
public class ConsultaAlumnoAuditoria extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_consulta", nullable = false)
    private OffsetDateTime fechaConsulta;

    @Column(name = "nombre_consultor", nullable = false, length = 200)
    private String nombreConsultor;

    @Column(name = "cedula_consultada", nullable = false, length = 10)
    private String cedulaConsultada;

    @Column(name = "alumno_id")
    private Long alumnoId;

    @Column(name = "nombre_alumno", length = 300)
    private String nombreAlumno;

    @Column(name = "resultado_encontrado", nullable = false)
    private Boolean resultadoEncontrado = false;

    public Long getId() { return id; }
    public OffsetDateTime getFechaConsulta() { return fechaConsulta; }
    public String getNombreConsultor() { return nombreConsultor; }
    public String getCedulaConsultada() { return cedulaConsultada; }
    public Long getAlumnoId() { return alumnoId; }
    public String getNombreAlumno() { return nombreAlumno; }
    public Boolean getResultadoEncontrado() { return resultadoEncontrado; }

    public void setId(Long id) { this.id = id; }
    public void setFechaConsulta(OffsetDateTime fechaConsulta) { this.fechaConsulta = fechaConsulta; }
    public void setNombreConsultor(String nombreConsultor) { this.nombreConsultor = nombreConsultor; }
    public void setCedulaConsultada(String cedulaConsultada) { this.cedulaConsultada = cedulaConsultada; }
    public void setAlumnoId(Long alumnoId) { this.alumnoId = alumnoId; }
    public void setNombreAlumno(String nombreAlumno) { this.nombreAlumno = nombreAlumno; }
    public void setResultadoEncontrado(Boolean resultadoEncontrado) { this.resultadoEncontrado = resultadoEncontrado; }
}

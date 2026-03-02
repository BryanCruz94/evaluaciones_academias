package com.nairbdev.academiasbackend.dto.alumnos;

import com.nairbdev.academiasbackend.entity.OperadorObjetivo;
import com.nairbdev.academiasbackend.entity.TipoValorPrueba;

import java.math.BigDecimal;

public class AlumnoPruebaFisicaDto {
    private Long id; // id de programa_prueba_fisica
    private String nombre;
    private String descripcion;
    private TipoValorPrueba tipoValor;
    private String unidad;
    private OperadorObjetivo operador;
    private BigDecimal objetivoValor;
    private String etiqueta;

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public TipoValorPrueba getTipoValor() { return tipoValor; }
    public String getUnidad() { return unidad; }
    public OperadorObjetivo getOperador() { return operador; }
    public BigDecimal getObjetivoValor() { return objetivoValor; }
    public String getEtiqueta() { return etiqueta; }

    public void setId(Long id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setTipoValor(TipoValorPrueba tipoValor) { this.tipoValor = tipoValor; }
    public void setUnidad(String unidad) { this.unidad = unidad; }
    public void setOperador(OperadorObjetivo operador) { this.operador = operador; }
    public void setObjetivoValor(BigDecimal objetivoValor) { this.objetivoValor = objetivoValor; }
    public void setEtiqueta(String etiqueta) { this.etiqueta = etiqueta; }
}
package com.nairbdev.academiasbackend.dto.programaPruebaFisica;

import com.nairbdev.academiasbackend.entity.Genero;
import com.nairbdev.academiasbackend.entity.TipoValorPrueba;
import com.nairbdev.academiasbackend.entity.OperadorObjetivo;

import java.math.BigDecimal;

public class ProgramaPruebaFisicaResponse {
    private Long id;
    private Long programaId;

    private Long pruebaFisicaId;
    private String pruebaNombre;
    private TipoValorPrueba tipoValor;
    private String unidad;

    private Integer orden;
    private BigDecimal objetivoValor;
    private OperadorObjetivo operador;
    private String etiqueta;
    private Genero genero;

    private Boolean activo;

    // getters/setters ...


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProgramaId() {
        return programaId;
    }

    public void setProgramaId(Long programaId) {
        this.programaId = programaId;
    }

    public Long getPruebaFisicaId() {
        return pruebaFisicaId;
    }

    public void setPruebaFisicaId(Long pruebaFisicaId) {
        this.pruebaFisicaId = pruebaFisicaId;
    }

    public String getPruebaNombre() {
        return pruebaNombre;
    }

    public void setPruebaNombre(String pruebaNombre) {
        this.pruebaNombre = pruebaNombre;
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

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
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

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
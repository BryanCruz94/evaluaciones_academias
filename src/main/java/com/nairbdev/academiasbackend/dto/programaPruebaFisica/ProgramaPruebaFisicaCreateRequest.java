package com.nairbdev.academiasbackend.dto.programaPruebaFisica;

import com.nairbdev.academiasbackend.entity.Genero;
import com.nairbdev.academiasbackend.entity.TipoValorPrueba;
import com.nairbdev.academiasbackend.entity.OperadorObjetivo;

import java.math.BigDecimal;

public class ProgramaPruebaFisicaCreateRequest {

    // Opción 1: usar catálogo existente
    private Long pruebaFisicaId;

    // Opción 2: crear prueba en catálogo al vuelo
    private String nombre;
    private TipoValorPrueba tipoValor;
    private String unidad;

    // Configuración para el programa
    private Integer orden;                 // obligatorio (>0)
    private BigDecimal objetivoValor;       // puede ser null si no aplica
    private OperadorObjetivo operador;      // debe venir junto con objetivoValor o ambos null
    private String etiqueta;

    // getters/setters ..

    public Long getPruebaFisicaId() {
        return pruebaFisicaId;
    }

    public void setPruebaFisicaId(Long pruebaFisicaId) {
        this.pruebaFisicaId = pruebaFisicaId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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


}

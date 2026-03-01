package com.nairbdev.academiasbackend.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum OperadorObjetivo {
    GE(">="),
    LE("<="),
    EQ("=");

    private final String simbolo;

    OperadorObjetivo(String simbolo) {
        this.simbolo = simbolo;
    }

    @JsonValue
    public String toJson() {
        return simbolo;
    }

    @JsonCreator
    public static OperadorObjetivo fromJson(String value) {
        if (value == null) return null;

        return switch (value.trim()) {
            case ">=", "GE" -> GE;
            case "<=", "LE" -> LE;
            case "=", "EQ" -> EQ;
            default -> throw new IllegalArgumentException("Operador inválido: " + value);
        };
    }

    public String getSimbolo() {
        return simbolo;
    }
}
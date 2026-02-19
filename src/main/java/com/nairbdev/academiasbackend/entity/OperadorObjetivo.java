package com.nairbdev.academiasbackend.entity;

public enum       OperadorObjetivo {
    GE(">="),
    LE("<="),
    EQ("=");

    private final String symbol;
    OperadorObjetivo(String symbol) { this.symbol = symbol; }
    public String getSymbol() { return symbol; }

    public static OperadorObjetivo fromSymbol(String s) {
        for (OperadorObjetivo op : values()) if (op.symbol.equals(s)) return op;
        throw new IllegalArgumentException("Operador inválido: " + s);
    }
}

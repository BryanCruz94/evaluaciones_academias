package com.nairbdev.academiasbackend.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class OperadorObjetivoConverter implements AttributeConverter<OperadorObjetivo, String> {
    @Override
    public String convertToDatabaseColumn(OperadorObjetivo attribute) {
        return attribute == null ? null : attribute.getSymbol();
    }

    @Override
    public OperadorObjetivo convertToEntityAttribute(String dbData) {
        return dbData == null ? null : OperadorObjetivo.fromSymbol(dbData);
    }
}

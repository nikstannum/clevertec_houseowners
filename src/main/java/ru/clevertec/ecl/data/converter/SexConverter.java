package ru.clevertec.ecl.data.converter;


import jakarta.persistence.AttributeConverter;
import ru.clevertec.ecl.data.entity.Sex;

public class SexConverter implements AttributeConverter<Sex, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Sex attribute) {
        switch (attribute) {
            case MALE -> {
                return 1;
            }
            case FEMALE -> {
                return 2;
            }
            default -> throw new IllegalArgumentException(attribute + " not supported yet");
        }
    }

    @Override
    public Sex convertToEntityAttribute(Integer dbData) {
        switch (dbData) {
            case 1 -> {
                return Sex.MALE;
            }
            case 2 -> {
                return Sex.FEMALE;
            }
            default -> throw new IllegalArgumentException(dbData + " not supported yet");
        }
    }
}

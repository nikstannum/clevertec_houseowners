package ru.clevertec.ecl.data.converter;

import jakarta.persistence.AttributeConverter;
import org.springframework.stereotype.Component;
import ru.clevertec.ecl.data.entity.Country;

import static ru.clevertec.ecl.data.entity.Country.BELARUS;
import static ru.clevertec.ecl.data.entity.Country.LATVIA;
import static ru.clevertec.ecl.data.entity.Country.POLAND;
import static ru.clevertec.ecl.data.entity.Country.RUSSIA;
import static ru.clevertec.ecl.data.entity.Country.UKRAINE;
import static ru.clevertec.ecl.data.entity.Country.USA;

@Component
public class CountryConverter implements AttributeConverter<Country, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Country attribute) {
        switch (attribute) {
            case BELARUS -> {
                return 1;
            }
            case USA -> {
                return 2;
            }
            case POLAND -> {
                return 3;
            }
            case RUSSIA -> {
                return 4;
            }
            case UKRAINE -> {
                return 5;
            }
            case LATVIA -> {
                return 6;
            }
            default -> throw new IllegalArgumentException(attribute + " not supported yet");
        }
    }

    @Override
    public Country convertToEntityAttribute(Integer dbData) {
        switch (dbData) {
            case 1 -> {
                return BELARUS;
            }
            case 2 -> {
                return USA;
            }
            case 3 -> {
                return POLAND;
            }
            case 4 -> {
                return RUSSIA;
            }
            case 5 -> {
                return UKRAINE;
            }
            case 6 -> {
                return LATVIA;
            }
            default -> throw new IllegalArgumentException(dbData + " not supported yet");
        }
    }
}

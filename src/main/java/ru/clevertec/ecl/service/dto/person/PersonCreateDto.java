package ru.clevertec.ecl.service.dto.person;

import java.util.Set;
import lombok.Data;
import ru.clevertec.ecl.data.entity.Sex;
import ru.clevertec.ecl.service.dto.house.HouseUpdateDto;

@Data
public class PersonCreateDto {

    private String name;

    private String surname;

    private Sex sex;

    private String passportSeries;

    private Integer passportNumber;

    private HouseUpdateDto residentsPlace;

    private Set<HouseUpdateDto> ownership;
}

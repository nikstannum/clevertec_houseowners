package ru.clevertec.ecl.service.dto.person;

import java.util.Set;
import java.util.UUID;
import lombok.Data;
import ru.clevertec.ecl.data.entity.Sex;
import ru.clevertec.ecl.service.dto.house.HouseUpdateDto;

@Data
public class PersonUpdateDto {

    private UUID uuid;

    private String name;

    private String surname;

    private Sex sex;

    private String passportSeries;

    private Integer passportNumber;

    private HouseUpdateDto residentsPlace;

    private Set<HouseUpdateDto> ownership;

}

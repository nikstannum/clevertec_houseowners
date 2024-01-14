package ru.clevertec.ecl.service.dto.person;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.Instant;
import java.util.UUID;
import lombok.Data;
import ru.clevertec.ecl.data.entity.Sex;

@Data
public class PersonReadDto {

    private UUID uuid;

    private String name;

    private String surname;

    private Sex sex;

    private String passportData;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant createDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant updateDate;

}

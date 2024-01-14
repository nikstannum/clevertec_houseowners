package ru.clevertec.ecl.service.dto.house;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.Data;
import ru.clevertec.ecl.service.dto.address.AddressDto;

@Data
public class HouseReadDto {

    private UUID uuid;

    private BigDecimal area;

    private AddressDto address;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant createDate;
}

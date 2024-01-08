package ru.clevertec.ecl.service.dto.house;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Data;
import ru.clevertec.ecl.service.dto.address.AddressDto;

@Data
public class HouseUpdateDto {
    private UUID uuid;

    private BigDecimal area;

    private AddressDto address;
}

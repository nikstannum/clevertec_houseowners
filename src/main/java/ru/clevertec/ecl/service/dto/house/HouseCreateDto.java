package ru.clevertec.ecl.service.dto.house;

import java.math.BigDecimal;
import lombok.Data;
import ru.clevertec.ecl.service.dto.address.AddressDto;

@Data
public class HouseCreateDto {

    private BigDecimal area;

    private AddressDto address;
}

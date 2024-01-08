package ru.clevertec.ecl.service.dto.address;

import lombok.Data;

@Data
public class AddressDto {

    private String country;

    private String city;

    private String street;

    private String number;
}

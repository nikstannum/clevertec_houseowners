package ru.clevertec.ecl.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.clevertec.ecl.data.entity.Address;
import ru.clevertec.ecl.data.entity.Country;
import ru.clevertec.ecl.service.dto.address.AddressDto;

@Mapper
public interface AddressMapper {


    @Mapping(source = "country", target = "country", qualifiedByName = "stringToCountry")
    Address addressDtoToAddress(AddressDto addressDto);

    @Named("stringToCountry")
    default Country stringToCountry(String country) {
        if (country == null) {
            return null;
        }

        return Country.valueOf(country);
    }


    @Mapping(source = "country", target = "country", qualifiedByName = "countryToString")
    AddressDto addressToAddressDto(Address address);

    @Named("countryToString")
    default String countryToString(Country country) {
        if (country == null) {
            return null;
        }

        return country.name();
    }
}

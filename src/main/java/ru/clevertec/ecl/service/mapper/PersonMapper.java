package ru.clevertec.ecl.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.ecl.data.entity.Person;
import ru.clevertec.ecl.service.dto.person.PersonCreateDto;
import ru.clevertec.ecl.service.dto.person.PersonReadDto;
import ru.clevertec.ecl.service.dto.person.PersonUpdateDto;

@Mapper(uses = HouseMapper.class)
public interface PersonMapper {

    @Mapping(target = "passportData", expression = "java(person.getPassportSeries() + \" \" + person.getPassportNumber().toString())")
    PersonReadDto personToPersonReadDto(Person person);

    Person personUpdateDtoToPerson(PersonUpdateDto personUpdateDto);

    Person personCreateDtoToPerson(PersonCreateDto personCreateDto);
}

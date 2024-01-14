package ru.clevertec.ecl.service;

import java.util.List;
import java.util.UUID;
import ru.clevertec.ecl.service.dto.person.PersonCreateDto;
import ru.clevertec.ecl.service.dto.person.PersonReadDto;
import ru.clevertec.ecl.service.dto.person.PersonUpdateDto;

public interface PersonService {

    PersonReadDto create(PersonCreateDto personCreateDto);


    PersonReadDto findById(Long id);

    PersonReadDto findByUuid(UUID uuid);

    List<PersonReadDto> search(String param, Integer page, Integer size);


    PersonReadDto patchPerson(Long id, PersonUpdateDto patches);

    PersonReadDto update(PersonUpdateDto personUpdateDto);

    void delete(Long id);
}

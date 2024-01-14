package ru.clevertec.ecl.web.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.service.PersonService;
import ru.clevertec.ecl.service.dto.person.PersonCreateDto;
import ru.clevertec.ecl.service.dto.person.PersonReadDto;
import ru.clevertec.ecl.service.dto.person.PersonUpdateDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/persons")
public class PersonRestController {

    private final PersonService personService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PersonReadDto create(@RequestBody PersonCreateDto personCreateDto) {
        return personService.create(personCreateDto);
    }

    @GetMapping(value = "/search")
    @ResponseStatus(HttpStatus.OK)
    public List<PersonReadDto> search(@RequestParam(name = "value") String param,
                                      @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                      @RequestParam(name = "size", required = false,
                                              defaultValue = "${spring.data.web.pageable.default-page-size}") Integer size) {
        return personService.search(param, page, size);
    }

    @PatchMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public PersonReadDto patch(@PathVariable("id") Long id, @RequestBody PersonUpdateDto patches) {
        return personService.patchPerson(id, patches);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PersonReadDto update(@RequestBody PersonUpdateDto personUpdateDto) {
        return personService.update(personUpdateDto);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        personService.delete(id);
    }
}

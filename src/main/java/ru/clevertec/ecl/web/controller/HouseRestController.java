package ru.clevertec.ecl.web.controller;

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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.service.HouseService;
import ru.clevertec.ecl.service.dto.house.HouseCreateDto;
import ru.clevertec.ecl.service.dto.house.HouseReadDto;
import ru.clevertec.ecl.service.dto.house.HouseUpdateDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/houses")
public class HouseRestController {

    private final HouseService houseService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public HouseReadDto create(@RequestBody HouseCreateDto houseCreateDto) {
        return houseService.create(houseCreateDto);
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public HouseReadDto findById(@PathVariable("id") Long id) {
        return houseService.findById(id);
    }

    @PatchMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public HouseReadDto patch(@PathVariable("id") Long id, @RequestBody HouseUpdateDto patches) {
        return houseService.patchHouse(id, patches);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public HouseReadDto update(@RequestBody HouseUpdateDto houseUpdateDto) {
        return houseService.update(houseUpdateDto);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        houseService.delete(id);
    }
}

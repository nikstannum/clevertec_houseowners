package ru.clevertec.ecl.service;

import java.util.List;
import java.util.UUID;
import ru.clevertec.ecl.service.dto.house.HouseCreateDto;
import ru.clevertec.ecl.service.dto.house.HouseReadDto;
import ru.clevertec.ecl.service.dto.house.HouseUpdateDto;

public interface HouseService {

    HouseReadDto create(HouseCreateDto houseCreateDto);

    HouseReadDto findById(Long id);

    HouseReadDto findByUuid(UUID uuid);

    List<HouseReadDto> findAll(int page, int size);


    HouseReadDto patchHouse(Long id, HouseUpdateDto patches);

    HouseReadDto update(HouseUpdateDto houseUpdateDto);

    void delete(Long id);
}

package ru.clevertec.ecl.service.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.ecl.data.entity.House;
import ru.clevertec.ecl.service.dto.house.HouseCreateDto;
import ru.clevertec.ecl.service.dto.house.HouseReadDto;
import ru.clevertec.ecl.service.dto.house.HouseUpdateDto;

@Mapper(uses = AddressMapper.class)
public interface HouseMapper {

    House houseCreateDtoToHouse(HouseCreateDto createDto);

    HouseReadDto houseToHouseReadDto(House house);

    House houseUpdateDtoToHouse(HouseUpdateDto houseUpdateDto);
}

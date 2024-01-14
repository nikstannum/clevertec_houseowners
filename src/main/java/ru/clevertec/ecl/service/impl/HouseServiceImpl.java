package ru.clevertec.ecl.service.impl;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.data.entity.Address;
import ru.clevertec.ecl.data.entity.House;
import ru.clevertec.ecl.data.repository.HouseRepository;
import ru.clevertec.ecl.exception.ExceptionCodeConstants;
import ru.clevertec.ecl.exception.NotFoundException;
import ru.clevertec.ecl.service.HouseService;
import ru.clevertec.ecl.service.dto.house.HouseCreateDto;
import ru.clevertec.ecl.service.dto.house.HouseReadDto;
import ru.clevertec.ecl.service.dto.house.HouseUpdateDto;
import ru.clevertec.ecl.service.mapper.HouseMapper;
import ru.clevertec.ecl.service.util.PagingUtil;
import ru.clevertec.ecl.service.util.PagingUtil.Paging;

@Service
@Transactional
@RequiredArgsConstructor
public class HouseServiceImpl implements HouseService {

    private final HouseRepository houseRepository;

    private final HouseMapper houseMapper;

    private final PagingUtil pagingUtil;

    @Override
    public HouseReadDto findById(Long id) {
        House house = houseRepository.findById(id).orElseThrow(() -> new NotFoundException("wasn't found house with id = " + id, ExceptionCodeConstants.HOUSE_NOT_FOUND));
        return houseMapper.houseToHouseReadDto(house);
    }

    @Override
    public HouseReadDto findByUuid(UUID uuid) {
        House house = houseRepository.findByUuid(uuid).orElseThrow(() -> new NotFoundException("wasn't found house with uuid = " + uuid,
                ExceptionCodeConstants.HOUSE_NOT_FOUND));
        return houseMapper.houseToHouseReadDto(house);
    }

    @Override
    public List<HouseReadDto> findAll(int page, int size) {
        Paging paging = pagingUtil.getPaging(page, size);
        int limit = paging.limit();
        int offset = paging.offset();
        List<House> all = houseRepository.findAll(limit, offset);
        return all.stream()
                .map(houseMapper::houseToHouseReadDto)
                .toList();
    }

    @Override
    public HouseReadDto patchHouse(Long id, HouseUpdateDto patches) {
        House existing = houseRepository.findById(id).orElseThrow(() -> new NotFoundException("wasn't house with id = " + id,
                ExceptionCodeConstants.HOUSE_NOT_FOUND));
        Address existingAddress = existing.getAddress();
        House patch = houseMapper.houseUpdateDtoToHouse(patches);
        Address patchAddress = patch.getAddress();
        mergeAddress(existingAddress, patchAddress);
        existing.setAddress(existingAddress);
        mergeHouse(existing, patch);
        return houseMapper.houseToHouseReadDto(houseRepository.update(existing));
    }

    private void mergeHouse(House existing, House patch) {
        if (patch.getUuid() != null) {
            existing.setUuid(patch.getUuid());
        }
        if (patch.getArea() != null) {
            existing.setArea(patch.getArea());
        }
        if (patch.getOwners() != null) {
            existing.getOwners().addAll(patch.getOwners());
        }
        if (patch.getResidents() != null) {
            existing.getResidents().addAll(patch.getResidents());
        }
    }

    private void mergeAddress(Address existingAddress, Address patchAddress) {
        if (patchAddress == null) {
            return;
        }
        if (patchAddress.getCountry() != null) {
            existingAddress.setCountry(patchAddress.getCountry());
        }
        if (patchAddress.getCity() != null) {
            existingAddress.setCity(patchAddress.getCity());
        }
        if (patchAddress.getStreet() != null) {
            existingAddress.setStreet(patchAddress.getStreet());
        }
        if (patchAddress.getNumber() != null) {
            existingAddress.setNumber(patchAddress.getNumber());
        }
    }

    @Override
    public HouseReadDto create(HouseCreateDto houseCreateDto) {
        House house = houseMapper.houseCreateDtoToHouse(houseCreateDto);
        House created = houseRepository.create(house);
        return houseMapper.houseToHouseReadDto(created);
    }

    @Override
    public HouseReadDto update(HouseUpdateDto houseUpdateDto) {
        House house = houseMapper.houseUpdateDtoToHouse(houseUpdateDto);
        House updated = houseRepository.update(house);
        return houseMapper.houseToHouseReadDto(updated);
    }

    @Override
    public void delete(Long id) {
        houseRepository.delete(id);
    }
}

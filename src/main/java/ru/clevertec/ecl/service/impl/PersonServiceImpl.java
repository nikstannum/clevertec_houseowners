package ru.clevertec.ecl.service.impl;

import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.data.entity.House;
import ru.clevertec.ecl.data.entity.Person;
import ru.clevertec.ecl.data.repository.HouseRepository;
import ru.clevertec.ecl.data.repository.PersonRepository;
import ru.clevertec.ecl.exception.ExceptionCodeConstants;
import ru.clevertec.ecl.exception.NotFoundException;
import ru.clevertec.ecl.service.PersonService;
import ru.clevertec.ecl.service.dto.house.HouseUpdateDto;
import ru.clevertec.ecl.service.dto.person.PersonCreateDto;
import ru.clevertec.ecl.service.dto.person.PersonReadDto;
import ru.clevertec.ecl.service.dto.person.PersonUpdateDto;
import ru.clevertec.ecl.service.mapper.HouseMapper;
import ru.clevertec.ecl.service.mapper.PersonMapper;
import ru.clevertec.ecl.service.util.PagingUtil;
import ru.clevertec.ecl.service.util.PagingUtil.Paging;

@Service
@Transactional
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final HouseRepository houseRepository;
    private final HouseMapper houseMapper;
    private final PersonMapper personMapper;
    private final PagingUtil pagingUtil;

    private static void addOwnership(Person existing, House house) {
        if (existing.getOwnership() != null) {
            existing.getOwnership().add(house);
        } else {
            Set<House> ownership = new HashSet<>();
            ownership.add(house);
            existing.setOwnership(ownership);
        }
        if (house.getOwners() != null) {
            house.getOwners().add(existing);
        } else {
            Set<Person> owners = new HashSet<>();
            owners.add(existing);
            house.setOwners(owners);
        }

    }

    private static void addResidence(Person existing, House house) {
        existing.setResidentsPlace(house);
        if (house.getResidents() != null) {
            house.getResidents().add(existing);
        } else {
            Set<Person> residents = new HashSet<>();
            residents.add(existing);
            house.setResidents(residents);
        }
    }

    @Override
    public PersonReadDto findById(Long id) {
        Person person = personRepository.findById(id).orElseThrow(() -> new NotFoundException("wasn't found person with id = " + id,
                ExceptionCodeConstants.PERSON_NOT_FOUND));
        return personMapper.personToPersonReadDto(person);
    }

    @Override
    public PersonReadDto findByUuid(UUID uuid) {
        Person person = personRepository.findByUuid(uuid).orElseThrow(() -> new NotFoundException("wasn't found person with uuid = " + uuid,
                ExceptionCodeConstants.PERSON_NOT_FOUND));
        return personMapper.personToPersonReadDto(person);
    }

    @Override
    public List<PersonReadDto> search(String param, Integer page, Integer size) {
        Paging paging = pagingUtil.getPaging(page, size);
        int limit = paging.limit();
        int offset = paging.offset();
        List<Person> persons = personRepository.search(param, limit, offset);
        return persons.stream()
                .map(personMapper::personToPersonReadDto)
                .toList();
    }

    @Override
    public PersonReadDto patchPerson(Long id, PersonUpdateDto patches) {
        Person existing = personRepository.findById(id).orElseThrow(() -> new NotFoundException("wasn't found person with id = " + id,
                ExceptionCodeConstants.PERSON_NOT_FOUND));
        HouseUpdateDto newResidentsPlaceDto = patches.getResidentsPlace();
        addHouse(existing, newResidentsPlaceDto, true);
        Set<HouseUpdateDto> patchesOwnership = patches.getOwnership();
        if (patchesOwnership != null && !patchesOwnership.isEmpty()) {
            for (HouseUpdateDto ownership : patchesOwnership) {
                addHouse(existing, ownership, false);
            }
        }
        mergePersons(existing, patches);
        return personMapper.personToPersonReadDto(personRepository.update(existing));
    }

    private void mergePersons(Person existing, PersonUpdateDto patches) {
        if (patches.getUuid() != null) {
            existing.setUuid(patches.getUuid());
        }
        if (patches.getName() != null) {
            existing.setName(patches.getName());
        }
        if (patches.getSurname() != null) {
            existing.setSurname(patches.getSurname());
        }
        if (patches.getSex() != null) {
            existing.setSex(patches.getSex());
        }
        if (patches.getPassportNumber() != null) {
            existing.setPassportNumber(patches.getPassportNumber());
        }
        if (patches.getPassportSeries() != null) {
            existing.setPassportSeries(patches.getPassportSeries());
        }
    }

    private void addHouse(Person existing, HouseUpdateDto houseUpdateDto, boolean isResidentsPlace) {
        if (houseUpdateDto == null || houseUpdateDto.getAddress() == null) {
            return;
        }
        House house = houseMapper.houseUpdateDtoToHouse(houseUpdateDto);
        Long houseId = houseRepository.getHouseIdByAddress(house.getAddress());
        house.setId(houseId);
        if (isResidentsPlace) {
            addResidence(existing, house);
        } else {
            addOwnership(existing, house);
        }
    }

    @Override
    public PersonReadDto create(PersonCreateDto personCreateDto) {
        Person person = personMapper.personCreateDtoToPerson(personCreateDto);
        Person created = personRepository.create(person);
        return personMapper.personToPersonReadDto(created);
    }

    @Override
    public PersonReadDto update(PersonUpdateDto personUpdateDto) {
        Person person = personMapper.personUpdateDtoToPerson(personUpdateDto);
        Person updated = personRepository.update(person);
        return personMapper.personToPersonReadDto(updated);
    }

    @Override
    public void delete(Long id) {
        personRepository.delete(id);
    }
}

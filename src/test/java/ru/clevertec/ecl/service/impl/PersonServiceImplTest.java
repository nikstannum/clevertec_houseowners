package ru.clevertec.ecl.service.impl;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.ecl.data.entity.Address;
import ru.clevertec.ecl.data.entity.Country;
import ru.clevertec.ecl.data.entity.House;
import ru.clevertec.ecl.data.entity.Person;
import ru.clevertec.ecl.data.entity.Sex;
import ru.clevertec.ecl.data.repository.HouseRepository;
import ru.clevertec.ecl.data.repository.PersonRepository;
import ru.clevertec.ecl.exception.NotFoundException;
import ru.clevertec.ecl.service.dto.address.AddressDto;
import ru.clevertec.ecl.service.dto.house.HouseUpdateDto;
import ru.clevertec.ecl.service.dto.person.PersonCreateDto;
import ru.clevertec.ecl.service.dto.person.PersonReadDto;
import ru.clevertec.ecl.service.dto.person.PersonUpdateDto;
import ru.clevertec.ecl.service.mapper.AddressMapperImpl;
import ru.clevertec.ecl.service.mapper.HouseMapper;
import ru.clevertec.ecl.service.mapper.HouseMapperImpl;
import ru.clevertec.ecl.service.mapper.PersonMapper;
import ru.clevertec.ecl.service.mapper.PersonMapperImpl;
import ru.clevertec.ecl.service.util.PagingUtil;
import ru.clevertec.ecl.service.util.PagingUtil.Paging;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonServiceImplTest {

    private final UUID uuid = UUID.randomUUID();
    @Mock
    private PersonRepository personRepository;
    @Mock
    private HouseRepository houseRepository;
    @Mock
    private HouseMapper houseMapper;
    @Mock
    private PersonMapper personMapper;
    @Mock
    private PagingUtil pagingUtil;
    @InjectMocks
    private PersonServiceImpl service;
    @Captor
    private ArgumentCaptor<Long> idCaptor;

    @BeforeEach
    void setUp() {
        service = new PersonServiceImpl(personRepository, houseRepository, houseMapper, personMapper, pagingUtil);
    }

    @Test
    void checkFindByIdShouldReturnNotNull() {
        // given
        when(personRepository.findById(1L)).thenReturn(Optional.of(new Person()));
        when(personMapper.personToPersonReadDto(any())).thenReturn(new PersonReadDto());

        // when
        PersonReadDto actual = service.findById(1L);

        // then
        assertThat(actual).isNotNull();
    }

    @Test
    void checkFindByIdShouldThrowNotFoundExc() {
        // given
        when(personRepository.findById(1L)).thenReturn(Optional.empty());

        // then
        assertThrows(NotFoundException.class, () -> service.findById(1L));
    }

    @Test
    void checkFindByUuidShouldReturnNotNull() {
        // given
        when(personRepository.findByUuid(any())).thenReturn(Optional.of(new Person()));
        when(personMapper.personToPersonReadDto(any())).thenReturn(new PersonReadDto());

        // when
        PersonReadDto actual = service.findByUuid(UUID.randomUUID());

        // then
        assertThat(actual).isNotNull();
    }

    @Test
    void checkFindByUuidShouldThrowNotFoundExc() {
        // given
        when(personRepository.findByUuid(any())).thenReturn(Optional.empty());

        // then
        assertThrows(NotFoundException.class, () -> service.findByUuid(UUID.randomUUID()));
    }

    @Test
    void checkSearchShouldHasSize2() {
        // given
        when(personRepository.search(any(), anyInt(), anyInt())).thenReturn(Arrays.asList(new Person(), new Person()));
        when(personMapper.personToPersonReadDto(any())).thenReturn(new PersonReadDto());
        when(pagingUtil.getPaging(anyInt(), anyInt())).thenReturn(new Paging(1, 2));

        // when
        List<PersonReadDto> actualList = service.search("something", 1, 2);

        // then
        assertThat(actualList).hasSize(2);
    }

    @Test
    void checkPatchPersonShouldHasFieldEquals() throws IllegalAccessException {
        // given
        HouseMapperImpl houseMapperImpl = new HouseMapperImpl();
        Field addressMapperField = houseMapperImpl.getClass().getDeclaredField("addressMapper");
        addressMapperField.setAccessible(true);
        addressMapperField.set(houseMapperImpl, new AddressMapperImpl());
        PersonMapperImpl personMapperImpl = new PersonMapperImpl();
        Field houseMapperField = personMapperImpl.getClass().getDeclaredField("houseMapper");
        houseMapperField.setAccessible(true);
        houseMapperField.set(personMapperImpl, houseMapperImpl);
        service = new PersonServiceImpl(personRepository, houseRepository, houseMapperImpl, personMapperImpl, pagingUtil);
        Person existing = getPerson();
        when(personRepository.findById(1L)).thenReturn(Optional.of(existing));

        Person updated = getUpdated();
        when(personRepository.update(any())).thenReturn(updated);

        // when
        PersonReadDto actual = service.patchPerson(1L, getPatches());
        PersonReadDto expected = personMapperImpl.personToPersonReadDto(updated);


        // then
        assertThat(actual).isEqualTo(expected);

    }

    private Person getUpdated() {
        Person person = new Person();
        person.setName("new name");
        person.setSurname("new surname");
        person.setUuid(uuid);
        person.setSex(Sex.MALE);
        person.setPassportSeries("KH");
        person.setPassportNumber(987654);

        House house = new House();
        Address address = new Address();
        address.setCountry(Country.USA);
        address.setCity("New York");
        address.setStreet("Broadway");
        address.setNumber("123");
        house.setAddress(address);
        person.setResidentsPlace(house);
        Set<House> ownership = new HashSet<>();
        ownership.add(house);
        person.setOwnership(ownership);
        return person;
    }

    private PersonUpdateDto getPatches() {
        PersonUpdateDto patches = new PersonUpdateDto();
        patches.setName("new name");
        patches.setSurname("new surname");
        patches.setUuid(uuid);
        patches.setSex(Sex.MALE);
        patches.setPassportSeries("KH");
        patches.setPassportNumber(987654);
        HouseUpdateDto houseUpdateDto = new HouseUpdateDto();
        AddressDto addressDto = new AddressDto();
        addressDto.setCountry("USA");
        addressDto.setCity("New York");
        addressDto.setStreet("Broadway");
        addressDto.setNumber("123");
        houseUpdateDto.setAddress(addressDto);
        patches.setResidentsPlace(houseUpdateDto);
        Set<HouseUpdateDto> ownership = new HashSet<>();
        ownership.add(houseUpdateDto);
        patches.setOwnership(ownership);
        return patches;
    }

    private Person getPerson() {
        House residence = getHouse();
        Person person = new Person();
        person.setResidentsPlace(residence);
        Set<Person> residents = new HashSet<>();
        residents.add(person);
        residence.setResidents(residents);
        House ownership = getHouse();
        Set<Person> owners = new HashSet<>();
        owners.add(person);
        ownership.setOwners(owners);
        Set<House> ownerships = new HashSet<>();
        ownerships.add(ownership);
        person.setOwnership(ownerships);
        person.setName("name");
        person.setSurname("surname");
        person.setSex(Sex.MALE);
        person.setPassportSeries("MP");
        person.setPassportNumber(123456);
        return person;
    }

    private House getHouse() {
        House house = new House();
        house.setId(1L);
        house.setUuid(uuid);
        house.setArea(BigDecimal.TEN);
        house.setAddress(getAddress());
        return house;
    }

    private Address getAddress() {
        Address address = new Address();
        address.setId(1L);
        address.setCountry(Country.BELARUS);
        address.setCity("Minsk");
        address.setStreet("Lenina");
        address.setNumber("1");
        return address;
    }

    @Test
    void checkCreateShouldReturnEquals() {
        // given
        when(personMapper.personCreateDtoToPerson(any())).thenReturn(new Person());
        Person person = getPerson();
        person.setUuid(uuid);
        when(personRepository.create(any())).thenReturn(person);
        PersonReadDto personReadDto = new PersonReadDto();
        personReadDto.setUuid(uuid);
        when(personMapper.personToPersonReadDto(person)).thenReturn(personReadDto);

        // when
        PersonReadDto actual = service.create(new PersonCreateDto());

        // then
        assertThat(actual.getUuid()).isEqualTo(uuid);
    }

    @Test
    void update() {
        // given
        Person person = new Person();
        person.setUuid(uuid);
        when(personMapper.personUpdateDtoToPerson(any())).thenReturn(person);
        when(personRepository.update(person)).thenReturn(person);
        PersonReadDto readDto = new PersonReadDto();
        readDto.setUuid(uuid);
        when(personMapper.personToPersonReadDto(person)).thenReturn(readDto);
        PersonUpdateDto updateDto = new PersonUpdateDto();
        updateDto.setUuid(uuid);

        // when
        PersonReadDto actual = service.update(updateDto);

        // then
        assertThat(actual.getUuid()).isEqualTo(uuid);
    }

    @Test
    void delete() {
        // when
        service.delete(1L);
        verify(personRepository).delete(idCaptor.capture());
        Long actual = idCaptor.getValue();

        // then
        assertThat(actual).isEqualTo(1L);
    }
}
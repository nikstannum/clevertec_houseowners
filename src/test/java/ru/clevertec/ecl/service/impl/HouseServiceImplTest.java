package ru.clevertec.ecl.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
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
import ru.clevertec.ecl.data.repository.impl.AddressRepositoryImpl;
import ru.clevertec.ecl.data.repository.impl.HouseRepositoryImpl;
import ru.clevertec.ecl.exception.NotFoundException;
import ru.clevertec.ecl.service.dto.address.AddressDto;
import ru.clevertec.ecl.service.dto.house.HouseCreateDto;
import ru.clevertec.ecl.service.dto.house.HouseReadDto;
import ru.clevertec.ecl.service.dto.house.HouseUpdateDto;
import ru.clevertec.ecl.service.mapper.HouseMapperImpl;
import ru.clevertec.ecl.service.util.PagingUtil;
import ru.clevertec.ecl.service.util.PagingUtil.Paging;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HouseServiceImplTest {

    private final UUID uuid = UUID.randomUUID();
    @Mock
    private HouseRepositoryImpl houseRepository;
    @Mock
    private AddressRepositoryImpl addressRepository;
    @Mock
    private PagingUtil pagingUtil;
    @Mock
    private HouseMapperImpl houseMapper;
    @InjectMocks
    private HouseServiceImpl service;
    @Captor
    private ArgumentCaptor<Long> idCaptor;

    @BeforeEach
    void setUp() {
        service = new HouseServiceImpl(houseRepository, addressRepository, houseMapper, pagingUtil);
    }

    @Test
    void checkFindByIdShouldReturnEquals() {
        // given
        when(houseRepository.findById(1L)).thenReturn(Optional.of(new House()));
        when(houseMapper.houseToHouseReadDto(any())).thenReturn(new HouseReadDto());

        // when
        HouseReadDto actual = service.findById(1L);

        // then
        assertThat(actual).isNotNull();
    }

    @Test
    void checkFindByIdShouldThrowNotFoundExc() {
        // given
        when(houseRepository.findById(1L)).thenReturn(Optional.empty());

        // then
        assertThrows(NotFoundException.class, () -> service.findById(1L));
    }

    @Test
    void checkFindByUuidShouldReturnNotNull() {
        // given
        when(houseRepository.findByUuid(any())).thenReturn(Optional.of(new House()));
        when(houseMapper.houseToHouseReadDto(any())).thenReturn(new HouseReadDto());

        // when
        HouseReadDto actual = service.findByUuid(UUID.randomUUID());

        // then
        assertThat(actual).isNotNull();
    }

    @Test
    void checkFindByUuidShouldThrowNotFoundExc() {
        // given
        when(houseRepository.findByUuid(any())).thenReturn(Optional.empty());

        // then
        assertThrows(NotFoundException.class, () -> service.findByUuid(UUID.randomUUID()));
    }

    @Test
    void checkFindAllShouldHasSize2() {
        // given
        when(houseRepository.findAll(1, 2)).thenReturn(List.of(new House(), new House()));
        when(houseMapper.houseToHouseReadDto(any())).thenReturn(new HouseReadDto());
        when(pagingUtil.getPaging(anyInt(), anyInt())).thenReturn(new Paging(1, 2));

        // when
        List<HouseReadDto> actual = service.findAll(1, 2);

        // then
        assertThat(actual).hasSize(2);
    }

    @Test
    void patchHouse() {
        // given
        when(houseMapper.houseUpdateDtoToHouse(getPatchHouse())).thenReturn(getHouse());
        House existing = getHouse();
        when(houseRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(houseRepository.update(any())).thenReturn(existing);
        when(houseMapper.houseToHouseReadDto(existing)).thenReturn(getHouseReadDto());

        // when
        HouseReadDto actual = service.patchHouse(1L, getPatchHouse());

        // then
        assertThat(actual).isEqualTo(getHouseReadDto());
    }

    private HouseReadDto getHouseReadDto() {
        HouseReadDto house = new HouseReadDto();
        house.setUuid(uuid);
        house.setArea(BigDecimal.TEN);
        house.setAddress(getPatchAddress());
        return house;
    }

    private HouseUpdateDto getPatchHouse() {
        HouseUpdateDto house = new HouseUpdateDto();
        house.setUuid(uuid);
        house.setArea(BigDecimal.TEN);
        house.setAddress(getPatchAddress());
        return house;
    }

    private AddressDto getPatchAddress() {
        AddressDto address = new AddressDto();
        address.setCountry("BELARUS");
        address.setCity("Minsk");
        address.setStreet("Lenina");
        address.setNumber("1");
        return address;
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
    void create() {
        // given
        House house = new House();
        when(houseMapper.houseCreateDtoToHouse(any())).thenReturn(house);
        house.setUuid(uuid);
        when(houseRepository.create(house)).thenReturn(house);
        HouseReadDto houseReadDto = new HouseReadDto();
        houseReadDto.setUuid(uuid);
        when(houseMapper.houseToHouseReadDto(house)).thenReturn(houseReadDto);

        // when
        HouseReadDto actual = service.create(new HouseCreateDto());

        // then
        assertThat(actual.getUuid()).isEqualTo(uuid);
    }

    @Test
    void update() {
        // given
        House house = new House();
        house.setUuid(uuid);
        when(houseMapper.houseUpdateDtoToHouse(any())).thenReturn(house);
        when(houseRepository.update(house)).thenReturn(house);
        HouseReadDto houseReadDto = new HouseReadDto();
        houseReadDto.setUuid(uuid);
        when(houseMapper.houseToHouseReadDto(house)).thenReturn(houseReadDto);
        HouseUpdateDto updateDto = new HouseUpdateDto();
        updateDto.setUuid(uuid);

        // when
        HouseReadDto actual = service.update(updateDto);

        // then
        assertThat(actual.getUuid()).isEqualTo(uuid);
    }

    @Test
    void delete() {
        // when
        service.delete(1L);
        verify(houseRepository).delete(idCaptor.capture());
        Long actual = idCaptor.getValue();

        // then
        assertThat(actual).isEqualTo(1L);
    }
}
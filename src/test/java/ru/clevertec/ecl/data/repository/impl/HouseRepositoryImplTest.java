package ru.clevertec.ecl.data.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.clevertec.ecl.TestConfig;
import ru.clevertec.ecl.data.entity.Address;
import ru.clevertec.ecl.data.entity.Country;
import ru.clevertec.ecl.data.entity.House;
import ru.clevertec.ecl.data.repository.HouseRepository;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringJUnitConfig(TestConfig.class)
class HouseRepositoryImplTest extends BaseContainer {

    @Autowired
    private HouseRepository repository;
    @PersistenceContext
    private EntityManager manager;

    @Test
    void create() {
        House house = getHouse();
        House created = repository.create(house);
        assertThat(created.getId()).isNotNull();
    }

    @Test
    void findById() {
        Optional<House> house = repository.findById(1L);
        assertThat(house).isPresent();
    }

    @Test
    void findAll() {
        List<House> all = repository.findAll(2, 0);
        assertThat(all).hasSize(2);
    }

    @Test
    void update() {
        House house = getHouse();
        house.setId(1L);
        house.setArea(BigDecimal.ONE);
        House updated = repository.update(house);
        assertThat(updated.getArea()).isEqualTo(BigDecimal.ONE);
    }

    @Test
    void delete() {
        repository.delete(1L);
        manager.flush();
    }

    @Test
    void getHouseIdByAddress() {
        Address address = new Address();
        address.setCountry(Country.BELARUS);
        address.setCity("Minsk");
        address.setStreet("Lenina");
        address.setNumber("11 A");
        Long houseIdByAddress = repository.getHouseIdByAddress(address);
        assertThat(houseIdByAddress).isEqualTo(1L);
    }

    private House getHouse() {
        House house = new House();
        house.setUuid(UUID.randomUUID());
        house.setArea(BigDecimal.TEN);
        house.setAddress(getAddress());
        return house;
    }

    private Address getAddress() {
        Address address = new Address();
        address.setCountry(Country.USA);
        address.setCity("Las Vegas");
        address.setStreet("Kommunisticheskay");
        address.setNumber("95/2");
        return address;
    }
}
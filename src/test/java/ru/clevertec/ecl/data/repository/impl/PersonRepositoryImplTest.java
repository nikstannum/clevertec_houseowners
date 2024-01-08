package ru.clevertec.ecl.data.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.clevertec.ecl.TestConfig;
import ru.clevertec.ecl.data.entity.Address;
import ru.clevertec.ecl.data.entity.Country;
import ru.clevertec.ecl.data.entity.House;
import ru.clevertec.ecl.data.entity.Person;
import ru.clevertec.ecl.data.entity.Sex;
import ru.clevertec.ecl.data.repository.PersonRepository;

@ExtendWith(SpringExtension.class)
@SpringJUnitConfig(TestConfig.class)
class PersonRepositoryImplTest extends BaseContainer {

    @Autowired
    private PersonRepository repository;
    @PersistenceContext
    private EntityManager manager;

    @Test
    void create() {
        Person person = getPerson();
        Person created = repository.create(person);
        Assertions.assertThat(created.getId()).isNotNull();
    }

    @Test
    void search() {
        List<Person> searched = repository.search("van", 5, 0);
        Assertions.assertThat(searched).isNotEmpty();
    }

    @Test
    void checkFindByIdShouldReturnEquals() {
        Optional<Person> byId = repository.findById(1L);
        Assertions.assertThat(byId).isPresent();
    }

    @Test
    void checkFindByIdShouldReturnEmpty() {
        Optional<Person> byId = repository.findById(-1L);
        Assertions.assertThat(byId).isEmpty();
    }


    @Test
    void findAll() {
        List<Person> all = repository.findAll(2, 2);
        Assertions.assertThat(all).hasSize(2);
    }

    @Test
    void update() {
        Person person = getPerson();
        person.setId(1L);
        Person updated = repository.update(person);
        Assertions.assertThat(updated.getPassportSeries()).isEqualTo("MP");
    }

    @Test
    void delete() {
        repository.delete(1L);
        manager.flush();
    }

    private Person getPerson() {
        Person person = new Person();
        person.setUuid(UUID.randomUUID());
        person.setSex(Sex.MALE);
        person.setName("name");
        person.setSurname("surname");
        person.setPassportSeries("MP");
        person.setPassportNumber(123456);
        person.setResidentsPlace(getHouse());
        return person;
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
package ru.clevertec.ecl.data.repository.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.clevertec.ecl.TestConfig;
import ru.clevertec.ecl.data.entity.Address;
import ru.clevertec.ecl.data.entity.Country;
import ru.clevertec.ecl.data.repository.AddressRepository;

@ExtendWith(SpringExtension.class)
@SpringJUnitConfig(TestConfig.class)
class AddressRepositoryImplTest extends BaseContainer {
    @Autowired
    private AddressRepository repository;

    @Test
    void getAddressIdByParam() {
        Address address = new Address();
        address.setCountry(Country.BELARUS);
        address.setCity("Minsk");
        address.setStreet("Lenina");
        address.setNumber("11 A");
        Long addressIdByParam = repository.getAddressIdByParam(address);
        Assertions.assertThat(addressIdByParam).isNotNull();
    }
}
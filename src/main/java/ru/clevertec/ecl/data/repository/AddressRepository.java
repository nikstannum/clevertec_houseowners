package ru.clevertec.ecl.data.repository;

import ru.clevertec.ecl.data.entity.Address;

public interface AddressRepository {

    Long getAddressIdByParam(Address address);
}

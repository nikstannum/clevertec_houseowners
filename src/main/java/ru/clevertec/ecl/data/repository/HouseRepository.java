package ru.clevertec.ecl.data.repository;

import java.util.Optional;
import java.util.UUID;
import ru.clevertec.ecl.data.entity.Address;
import ru.clevertec.ecl.data.entity.House;

public interface HouseRepository extends CrudRepository<House, Long> {
    Optional<House> findByUuid(UUID uuid);

    Long getHouseIdByAddress(Address address);
}

package ru.clevertec.ecl.data.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import ru.clevertec.ecl.data.entity.Person;

public interface PersonRepository extends CrudRepository<Person, Long> {

    Optional<Person> findByUuid(UUID uuid);

    List<Person> search(String param, int limit, int offset);
}

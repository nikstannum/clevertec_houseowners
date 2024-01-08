package ru.clevertec.ecl.data.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.data.entity.Person;
import ru.clevertec.ecl.data.repository.PersonRepository;
import ru.clevertec.ecl.data.repository.util.CriteriaQueryBuilder;
import ru.clevertec.ecl.exception.ExceptionCodeConstants;
import ru.clevertec.ecl.exception.NotFoundException;

@Repository
@RequiredArgsConstructor
public class PersonRepositoryImpl implements PersonRepository {

    private final CriteriaQueryBuilder<Person> queryBuilder;
    @PersistenceContext
    private EntityManager manager;

    @Override
    public Person create(Person entity) {
        manager.persist(entity);
        return entity;
    }

    @Override
    public List<Person> search(String param, int limit, int offset) {
        TypedQuery<Person> query = queryBuilder.search(Person.class, param);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    @Override
    public Optional<Person> findById(Long id) {
        return Optional.ofNullable(manager.find(Person.class, id));
    }

    @Override
    public Optional<Person> findByUuid(UUID uuid) {
        return Optional.of(manager.find(Person.class, uuid));
    }

    @Override
    public List<Person> findAll(int limit, int offset) {
        TypedQuery<Person> selectQuery = manager.createQuery("SELECT p from Person p ORDER BY p.id", Person.class);
        selectQuery.setMaxResults(limit);
        selectQuery.setFirstResult(offset);
        return selectQuery.getResultList();
    }

    @Override
    public Person update(Person entity) {
        return manager.merge(entity);
    }

    @Override
    public void delete(Long id) {
        Person person = manager.find(Person.class, id);
        if (person != null) {
            manager.remove(person);
        } else {
            throw new NotFoundException("wasn't found person with id = " + id, ExceptionCodeConstants.PERSON_NOT_FOUND);
        }
    }
}

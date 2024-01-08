package ru.clevertec.ecl.data.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.data.entity.Address;
import ru.clevertec.ecl.data.entity.Country;
import ru.clevertec.ecl.data.entity.House;
import ru.clevertec.ecl.data.repository.HouseRepository;
import ru.clevertec.ecl.exception.ExceptionCodeConstants;
import ru.clevertec.ecl.exception.NotFoundException;

@Repository
@RequiredArgsConstructor
public class HouseRepositoryImpl implements HouseRepository {

    private static final String FIND_ALL = """
            SELECT h."uuid" , h.area, h.create_date, c."name" , a.city , a.street, a."number"
            FROM houses h
            JOIN addresses a ON h.address_id = a.id
            JOIN countries c ON c.id = a.country_id
            LIMIT :limit
            OFFSET :offset
            """;
    private final NamedParameterJdbcTemplate template;
    @PersistenceContext
    private EntityManager manager;

    @Override
    public House create(House entity) {
        manager.persist(entity);
        return entity;
    }

    @Override
    public Optional<House> findById(Long id) {
        return Optional.ofNullable(manager.find(House.class, id));
    }

    @Override
    public Optional<House> findByUuid(UUID uuid) {
        return Optional.of(manager.find(House.class, uuid));
    }

    @Override
    public List<House> findAll(int limit, int offset) {
        Map<String, Integer> params = new HashMap<>();
        params.put("limit", limit);
        params.put("offset", offset);
        List<House> houses = new ArrayList<>();
        return template.query(FIND_ALL, params, rs -> {
            while (rs.next()) {
                House house = new House();
                house.setUuid(rs.getObject("uuid", UUID.class));
                house.setArea(rs.getBigDecimal("area"));
                house.setCreateDate(rs.getTimestamp("create_date").toLocalDateTime().atZone(TimeZone.getTimeZone("UTC").toZoneId()).toInstant());
                Address address = new Address();
                address.setCountry(Country.valueOf(rs.getString("name")));
                address.setCity(rs.getString("city"));
                address.setStreet(rs.getString("street"));
                address.setNumber(rs.getString("number"));
                house.setAddress(address);
                houses.add(house);
            }
            return houses;
        });
    }

    @Override
    public House update(House entity) {
        return manager.merge(entity);
    }

    @Override
    public void delete(Long id) {
        House house = manager.find(House.class, id);
        if (house == null) {
            throw new NotFoundException("wasn't found house with id = " + id, ExceptionCodeConstants.HOUSE_NOT_FOUND);
        } else {
            manager.remove(house);
        }
    }

    @Override
    public Long getHouseIdByAddress(Address address) {
        try (Session session = manager.unwrap(Session.class)) {
            NativeQuery<Long> getHouseIdQuery = session
                    .createNativeQuery("SELECT * FROM get_house_id(:country_name, :_city, :_street, :house_number)", Long.class);
            getHouseIdQuery
                    .setParameter("country_name", address.getCountry().toString())
                    .setParameter("_city", address.getCity())
                    .setParameter("_street", address.getStreet())
                    .setParameter("house_number", address.getNumber());
            return getHouseIdQuery.getSingleResult();
        }
    }
}

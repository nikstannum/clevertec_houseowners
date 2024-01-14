package ru.clevertec.ecl.data.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.data.entity.Address;
import ru.clevertec.ecl.data.repository.AddressRepository;

@Repository
@RequiredArgsConstructor
public class AddressRepositoryImpl implements AddressRepository {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Long getAddressIdByParam(Address address) {
        try (Session session = manager.unwrap(Session.class)) {
            NativeQuery<Long> getAddressIdQuery = session
                    .createNativeQuery("SELECT * FROM get_address_id(:country_name, :_city, :_street, :house_number)", Long.class);
            getAddressIdQuery
                    .setParameter("country_name", address.getCountry().toString())
                    .setParameter("_city", address.getCity())
                    .setParameter("_street", address.getStreet())
                    .setParameter("house_number", address.getNumber());
            return getAddressIdQuery.getSingleResult();
        }
    }

}

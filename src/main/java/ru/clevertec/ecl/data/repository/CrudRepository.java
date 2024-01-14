package ru.clevertec.ecl.data.repository;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T, ID> {
    /**
     * serializes an object to the database
     *
     * @param entity the serializable object
     * @return the same object from the database
     */
    T create(T entity);

    /**
     * serializes an object from the database
     *
     * @param id the object id
     * @return this object
     */
    Optional<T> findById(ID id);

    /**
     * serializes a list of objects from the database
     *
     * @param limit  the max number of objects
     * @param offset the quantity of objects are behind
     * @return this object
     */
    List<T> findAll(int limit, int offset);

    /**
     * updates an object in the database
     *
     * @param entity the object itself
     * @return this updated object
     */
    T update(T entity);

    /**
     * @param id object id
     */
    void delete(ID id);
}

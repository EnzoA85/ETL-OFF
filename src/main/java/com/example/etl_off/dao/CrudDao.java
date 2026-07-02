package com.example.etl_off.dao;

import java.util.List;
import java.util.Optional;

/**
 * Common read/write contract for application DAOs.
 *
 * @param <T> entity type handled by the DAO
 */
public interface CrudDao<T> {

    /**
     * Persists the given entity.
     *
     * @param entity entity to persist
     * @return persisted entity
     */
    T save(T entity);

    /**
     * Persists several entities at once.
     *
     * @param entities entities to persist
     * @return persisted entities
     */
    List<T> saveAll(Iterable<T> entities);

    /**
     * Finds an entity by its technical identifier.
     *
     * @param id technical identifier
     * @return matching entity when present
     */
    Optional<T> findById(Long id);

    /**
     * Retrieves all entities.
     *
     * @return all persisted entities
     */
    List<T> findAll();
}

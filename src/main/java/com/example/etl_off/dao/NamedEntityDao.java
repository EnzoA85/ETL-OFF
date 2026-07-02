package com.example.etl_off.dao;

import java.util.Optional;

/**
 * DAO contract for entities identified by a unique business name.
 *
 * @param <T> entity type handled by the DAO
 */
public interface NamedEntityDao<T> extends CrudDao<T> {

    Optional<T> findByNom(String nom);

    boolean existsByNom(String nom);
}

package com.example.etl_off.service;

import com.example.etl_off.dao.NamedEntityDao;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Shared service behavior for entities with a unique name.
 *
 * @param <T> entity type handled by the service
 */
public abstract class AbstractNamedEntityService<T> {

    private final NamedEntityDao<T> dao;
    private final Function<String, T> entityFactory;

    protected AbstractNamedEntityService(NamedEntityDao<T> dao, Function<String, T> entityFactory) {
        this.dao = dao;
        this.entityFactory = entityFactory;
    }

    /**
     * Finds an entity by name or creates it when missing.
     *
     * @param nom unique business name
     * @return existing or newly persisted entity
     */
    @Transactional
    public T findOrCreateByNom(String nom) {
        String normalizedName = normalizeRequiredName(nom);
        return dao.findByNom(normalizedName)
                .orElseGet(() -> dao.save(entityFactory.apply(normalizedName)));
    }

    /**
     * Finds an entity by its unique name.
     *
     * @param nom unique business name
     * @return matching entity when present
     */
    @Transactional(readOnly = true)
    public Optional<T> findByNom(String nom) {
        if (!StringUtils.hasText(nom)) {
            return Optional.empty();
        }
        return dao.findByNom(nom.trim());
    }

    /**
     * Returns all persisted entities of this type.
     *
     * @return entity list
     */
    @Transactional(readOnly = true)
    public List<T> findAll() {
        return dao.findAll();
    }

    /**
     * Persists the provided entity.
     *
     * @param entity entity to save
     * @return persisted entity
     */
    @Transactional
    public T save(T entity) {
        return dao.save(entity);
    }

    /**
     * Persists several entities in one repository call.
     *
     * @param entities entities to persist
     * @return persisted entities
     */
    @Transactional
    public List<T> saveAll(Iterable<T> entities) {
        return dao.saveAll(entities);
    }

    private String normalizeRequiredName(String nom) {
        if (!StringUtils.hasText(nom)) {
            throw new IllegalArgumentException("Le nom ne peut pas etre vide.");
        }
        return nom.trim();
    }
}

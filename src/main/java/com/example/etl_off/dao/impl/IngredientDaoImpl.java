package com.example.etl_off.dao.impl;

import com.example.etl_off.dao.IngredientDao;
import com.example.etl_off.entity.Ingredient;
import com.example.etl_off.repository.IngredientRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * Spring Data based implementation of {@link IngredientDao}.
 */
@Repository
public class IngredientDaoImpl implements IngredientDao {

    private final IngredientRepository ingredientRepository;

    public IngredientDaoImpl(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public Ingredient save(Ingredient entity) {
        return ingredientRepository.save(entity);
    }

    @Override
    public List<Ingredient> saveAll(Iterable<Ingredient> entities) {
        return ingredientRepository.saveAll(entities);
    }

    @Override
    public Optional<Ingredient> findById(Long id) {
        return ingredientRepository.findById(id);
    }

    @Override
    public List<Ingredient> findAll() {
        return ingredientRepository.findAll();
    }

    @Override
    public Optional<Ingredient> findByNom(String nom) {
        return ingredientRepository.findByNomIngredientIgnoreCase(nom);
    }

    @Override
    public boolean existsByNom(String nom) {
        return ingredientRepository.existsByNomIngredientIgnoreCase(nom);
    }
}

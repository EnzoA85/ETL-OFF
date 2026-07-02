package com.example.etl_off.service;

import com.example.etl_off.dao.IngredientDao;
import com.example.etl_off.entity.Ingredient;
import org.springframework.stereotype.Service;

/**
 * Application service for ingredients.
 */
@Service
public class IngredientService extends AbstractNamedEntityService<Ingredient> {

    public IngredientService(IngredientDao ingredientDao) {
        super(ingredientDao, Ingredient::new);
    }
}

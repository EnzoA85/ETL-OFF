package com.example.etl_off.service;

import com.example.etl_off.dto.TopItemResponse;
import com.example.etl_off.repository.AdditifRepository;
import com.example.etl_off.repository.AllergeneRepository;
import com.example.etl_off.repository.IngredientRepository;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Application service exposing the most frequent normalized reference values.
 */
@Service
public class ReferenceTopService {

    private static final int DEFAULT_LIMIT = 10;
    private static final int MAX_LIMIT = 100;

    private final IngredientRepository ingredientRepository;
    private final AllergeneRepository allergeneRepository;
    private final AdditifRepository additifRepository;

    public ReferenceTopService(IngredientRepository ingredientRepository,
                               AllergeneRepository allergeneRepository,
                               AdditifRepository additifRepository) {
        this.ingredientRepository = ingredientRepository;
        this.allergeneRepository = allergeneRepository;
        this.additifRepository = additifRepository;
    }

    /**
     * Returns the most common ingredients.
     *
     * @param limit maximum number of rows
     * @return top ingredients
     */
    @Transactional(readOnly = true)
    public List<TopItemResponse> findTopIngredients(Integer limit) {
        return ingredientRepository.findTopIngredients(limitToPageable(limit));
    }

    /**
     * Returns the most common allergens.
     *
     * @param limit maximum number of rows
     * @return top allergens
     */
    @Transactional(readOnly = true)
    public List<TopItemResponse> findTopAllergenes(Integer limit) {
        return allergeneRepository.findTopAllergenes(limitToPageable(limit));
    }

    /**
     * Returns the most common additives.
     *
     * @param limit maximum number of rows
     * @return top additives
     */
    @Transactional(readOnly = true)
    public List<TopItemResponse> findTopAdditifs(Integer limit) {
        return additifRepository.findTopAdditifs(limitToPageable(limit));
    }

    private Pageable limitToPageable(Integer limit) {
        int effectiveLimit = limit == null ? DEFAULT_LIMIT : limit;
        if (effectiveLimit < 1) {
            effectiveLimit = DEFAULT_LIMIT;
        }
        return PageRequest.of(0, Math.min(effectiveLimit, MAX_LIMIT));
    }
}

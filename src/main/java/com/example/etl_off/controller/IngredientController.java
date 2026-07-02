package com.example.etl_off.controller;

import com.example.etl_off.dto.TopItemResponse;
import com.example.etl_off.service.ReferenceTopService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller exposing ingredient rankings.
 */
@RestController
@RequestMapping("/ingredients")
public class IngredientController {

    private final ReferenceTopService referenceTopService;

    public IngredientController(ReferenceTopService referenceTopService) {
        this.referenceTopService = referenceTopService;
    }

    /**
     * Returns the most frequent ingredients.
     *
     * @param limit maximum number of ingredients
     * @return top ingredients
     */
    @GetMapping("/top")
    public List<TopItemResponse> topIngredients(@RequestParam(required = false) Integer limit) {
        return referenceTopService.findTopIngredients(limit);
    }
}

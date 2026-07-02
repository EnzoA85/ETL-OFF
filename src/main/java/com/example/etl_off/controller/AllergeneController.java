package com.example.etl_off.controller;

import com.example.etl_off.dto.TopItemResponse;
import com.example.etl_off.service.ReferenceTopService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller exposing allergen rankings.
 */
@RestController
@RequestMapping("/allergens")
public class AllergeneController {

    private final ReferenceTopService referenceTopService;

    public AllergeneController(ReferenceTopService referenceTopService) {
        this.referenceTopService = referenceTopService;
    }

    /**
     * Returns the most frequent allergens.
     *
     * @param limit maximum number of allergens
     * @return top allergens
     */
    @GetMapping("/top")
    public List<TopItemResponse> topAllergenes(@RequestParam(required = false) Integer limit) {
        return referenceTopService.findTopAllergenes(limit);
    }
}

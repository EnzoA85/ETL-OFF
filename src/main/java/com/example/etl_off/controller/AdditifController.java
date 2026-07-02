package com.example.etl_off.controller;

import com.example.etl_off.dto.TopItemResponse;
import com.example.etl_off.service.ReferenceTopService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller exposing additive rankings.
 */
@RestController
@RequestMapping("/additives")
public class AdditifController {

    private final ReferenceTopService referenceTopService;

    public AdditifController(ReferenceTopService referenceTopService) {
        this.referenceTopService = referenceTopService;
    }

    /**
     * Returns the most frequent additives.
     *
     * @param limit maximum number of additives
     * @return top additives
     */
    @GetMapping("/top")
    public List<TopItemResponse> topAdditifs(@RequestParam(required = false) Integer limit) {
        return referenceTopService.findTopAdditifs(limit);
    }
}

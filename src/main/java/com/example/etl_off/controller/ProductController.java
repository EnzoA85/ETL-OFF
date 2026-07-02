package com.example.etl_off.controller;

import com.example.etl_off.dto.ProductResponse;
import com.example.etl_off.service.ProduitService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller exposing product rankings.
 */
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProduitService produitService;

    public ProductController(ProduitService produitService) {
        this.produitService = produitService;
    }

    /**
     * Returns the best products for a brand.
     *
     * @param brand brand name
     * @param limit maximum number of products
     * @return matching products
     */
    @GetMapping("/top-by-brand")
    public List<ProductResponse> topByBrand(
            @RequestParam String brand,
            @RequestParam(required = false) Integer limit) {
        return produitService.findTopByBrand(brand, limit);
    }

    /**
     * Returns the best products for a category.
     *
     * @param category category name
     * @param limit maximum number of products
     * @return matching products
     */
    @GetMapping("/top-by-category")
    public List<ProductResponse> topByCategory(
            @RequestParam String category,
            @RequestParam(required = false) Integer limit) {
        return produitService.findTopByCategory(category, limit);
    }

    /**
     * Returns the best products matching both a brand and a category.
     *
     * @param brand brand name
     * @param category category name
     * @param limit maximum number of products
     * @return matching products
     */
    @GetMapping("/top-by-brand-category")
    public List<ProductResponse> topByBrandAndCategory(
            @RequestParam String brand,
            @RequestParam String category,
            @RequestParam(required = false) Integer limit) {
        return produitService.findTopByBrandAndCategory(brand, category, limit);
    }
}

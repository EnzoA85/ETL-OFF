package com.example.etl_off.service;

import com.example.etl_off.dao.ProduitDao;
import com.example.etl_off.dto.ProductResponse;
import com.example.etl_off.entity.Produit;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Application service for products.
 */
@Service
public class ProduitService {

    private static final int DEFAULT_LIMIT = 10;
    private static final int MAX_LIMIT = 100;

    private final ProduitDao produitDao;

    public ProduitService(ProduitDao produitDao) {
        this.produitDao = produitDao;
    }

    /**
     * Persists a product with its relations.
     *
     * @param produit product to persist
     * @return persisted product
     */
    @Transactional
    public Produit save(Produit produit) {
        return produitDao.save(produit);
    }

    /**
     * Persists products in batches when supported by the JPA provider.
     *
     * @param produits products to persist
     * @return persisted products
     */
    @Transactional
    public List<Produit> saveAll(Iterable<Produit> produits) {
        return produitDao.saveAll(produits);
    }

    /**
     * Finds a product by its technical identifier.
     *
     * @param id technical identifier
     * @return matching product when present
     */
    @Transactional(readOnly = true)
    public Optional<Produit> findById(Long id) {
        return produitDao.findById(id);
    }

    /**
     * Returns all products.
     *
     * @return product list
     */
    @Transactional(readOnly = true)
    public List<Produit> findAll() {
        return produitDao.findAll();
    }

    /**
     * Returns the best products for a brand.
     *
     * @param brand brand name
     * @param limit maximum number of products
     * @return matching products ordered by nutrition score
     */
    @Transactional(readOnly = true)
    public List<ProductResponse> findTopByBrand(String brand, Integer limit) {
        requireText(brand, "La marque est obligatoire.");
        return produitDao.findTopByBrand(brand.trim(), limitToPageable(limit)).stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Returns the best products for a category.
     *
     * @param category category name
     * @param limit maximum number of products
     * @return matching products ordered by nutrition score
     */
    @Transactional(readOnly = true)
    public List<ProductResponse> findTopByCategory(String category, Integer limit) {
        requireText(category, "La categorie est obligatoire.");
        return produitDao.findTopByCategory(category.trim(), limitToPageable(limit)).stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Returns the best products matching both a brand and a category.
     *
     * @param brand brand name
     * @param category category name
     * @param limit maximum number of products
     * @return matching products ordered by nutrition score
     */
    @Transactional(readOnly = true)
    public List<ProductResponse> findTopByBrandAndCategory(String brand, String category, Integer limit) {
        requireText(brand, "La marque est obligatoire.");
        requireText(category, "La categorie est obligatoire.");
        return produitDao.findTopByBrandAndCategory(brand.trim(), category.trim(), limitToPageable(limit)).stream()
                .map(this::toResponse)
                .toList();
    }

    private ProductResponse toResponse(Produit produit) {
        return new ProductResponse(
                produit.getId(),
                produit.getNomProduit(),
                produit.getScoreNutritionnel(),
                produit.getEnergie(),
                produit.getMatiereGrasse(),
                produit.getCategorie().getNomCategorie(),
                produit.getMarque().getNomMarque());
    }

    private Pageable limitToPageable(Integer limit) {
        int effectiveLimit = limit == null ? DEFAULT_LIMIT : limit;
        if (effectiveLimit < 1) {
            effectiveLimit = DEFAULT_LIMIT;
        }
        return PageRequest.of(0, Math.min(effectiveLimit, MAX_LIMIT));
    }

    private void requireText(String value, String message) {
        if (!StringUtils.hasText(value)) {
            throw new IllegalArgumentException(message);
        }
    }
}

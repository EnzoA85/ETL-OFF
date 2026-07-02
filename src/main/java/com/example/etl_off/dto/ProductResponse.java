package com.example.etl_off.dto;

import java.math.BigDecimal;

/**
 * REST representation of a product.
 */
public class ProductResponse {

    private Long id;
    private String nomProduit;
    private String scoreNutritionnel;
    private BigDecimal energie;
    private BigDecimal matiereGrasse;
    private String categorie;
    private String marque;

    public ProductResponse(Long id,
                           String nomProduit,
                           String scoreNutritionnel,
                           BigDecimal energie,
                           BigDecimal matiereGrasse,
                           String categorie,
                           String marque) {
        this.id = id;
        this.nomProduit = nomProduit;
        this.scoreNutritionnel = scoreNutritionnel;
        this.energie = energie;
        this.matiereGrasse = matiereGrasse;
        this.categorie = categorie;
        this.marque = marque;
    }

    public Long getId() {
        return id;
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public String getScoreNutritionnel() {
        return scoreNutritionnel;
    }

    public BigDecimal getEnergie() {
        return energie;
    }

    public BigDecimal getMatiereGrasse() {
        return matiereGrasse;
    }

    public String getCategorie() {
        return categorie;
    }

    public String getMarque() {
        return marque;
    }
}

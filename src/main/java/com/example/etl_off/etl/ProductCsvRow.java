package com.example.etl_off.etl;

import java.math.BigDecimal;
import java.util.Set;

/**
 * Cleaned representation of one Open Food Facts CSV row.
 */
public class ProductCsvRow {

    private final String categorie;
    private final String marque;
    private final String nomProduit;
    private final String scoreNutritionnel;
    private final BigDecimal energie;
    private final BigDecimal matiereGrasse;
    private final Set<String> ingredients;
    private final Set<String> allergenes;
    private final Set<String> additifs;

    public ProductCsvRow(String categorie,
                         String marque,
                         String nomProduit,
                         String scoreNutritionnel,
                         BigDecimal energie,
                         BigDecimal matiereGrasse,
                         Set<String> ingredients,
                         Set<String> allergenes,
                         Set<String> additifs) {
        this.categorie = categorie;
        this.marque = marque;
        this.nomProduit = nomProduit;
        this.scoreNutritionnel = scoreNutritionnel;
        this.energie = energie;
        this.matiereGrasse = matiereGrasse;
        this.ingredients = ingredients;
        this.allergenes = allergenes;
        this.additifs = additifs;
    }

    public String getCategorie() {
        return categorie;
    }

    public String getMarque() {
        return marque;
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

    public Set<String> getIngredients() {
        return ingredients;
    }

    public Set<String> getAllergenes() {
        return allergenes;
    }

    public Set<String> getAdditifs() {
        return additifs;
    }
}

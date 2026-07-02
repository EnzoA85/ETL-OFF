package com.example.etl_off.etl;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class OpenFoodFactsCsvParserTest {

    private final OpenFoodFactsCsvParser parser = new OpenFoodFactsCsvParser();

    @Test
    void shouldCleanSpecialCharactersPercentagesAndParenthesis() {
        String line = "Categorie|Marque|Produit|b|Sucre* 15%, farine 50%, _Mais_ 35%, Pate (Farine 50%, Sucre 20%, Oeufs 30%)|100|2||||||||||||||||||||||Gluten, _Lait_|E100 - Curcumine|";

        ProductCsvRow row = parser.parseLine(line).orElseThrow();

        assertThat(row.getIngredients()).containsExactly("Sucre", "farine", "Mais", "Pate");
        assertThat(row.getAllergenes()).containsExactly("Gluten", "Lait");
        assertThat(row.getAdditifs()).containsExactly("E100 - Curcumine");
        assertThat(row.getScoreNutritionnel()).isEqualTo("B");
    }
}

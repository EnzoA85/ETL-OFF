package com.example.etl_off.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Unique ingredient extracted from product compositions.
 */
@Entity
@Table(name = "ingredient")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ingredient")
    private Long id;

    @Column(name = "nom_ingredient", nullable = false, unique = true)
    private String nomIngredient;

    /**
     * Creates an ingredient with its unique business name.
     *
     * @param nomIngredient ingredient name
     */
    public Ingredient(String nomIngredient) {
        this.nomIngredient = nomIngredient;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Ingredient ingredient)) {
            return false;
        }
        return id != null && id.equals(ingredient.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

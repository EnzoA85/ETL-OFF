package com.example.etl_off.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Food product enriched with nutrition data and normalized relations.
 */
@Entity
@Table(name = "produit")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_produit")
    private Long id;

    @Column(name = "nom_produit", nullable = false)
    private String nomProduit;

    @Column(name = "score_nutritionnel", length = 1)
    private String scoreNutritionnel;

    @Column(name = "energie", precision = 12, scale = 2)
    private BigDecimal energie;

    @Column(name = "matiere_grasse", precision = 10, scale = 2)
    private BigDecimal matiereGrasse;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categorie_id", nullable = false)
    @ToString.Exclude
    private Categorie categorie;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "marque_id", nullable = false)
    @ToString.Exclude
    private Marque marque;

    @ManyToMany
    @JoinTable(
            name = "produit_ingredient",
            joinColumns = @JoinColumn(name = "id_produit"),
            inverseJoinColumns = @JoinColumn(name = "id_ingredient"))
    @ToString.Exclude
    private Set<Ingredient> ingredients = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "produit_allergene",
            joinColumns = @JoinColumn(name = "id_produit"),
            inverseJoinColumns = @JoinColumn(name = "id_allergene"))
    @ToString.Exclude
    private Set<Allergene> allergenes = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "produit_additif",
            joinColumns = @JoinColumn(name = "id_produit"),
            inverseJoinColumns = @JoinColumn(name = "id_additif"))
    @ToString.Exclude
    private Set<Additif> additifs = new HashSet<>();

    /**
     * Creates a product linked to its mandatory category and brand.
     *
     * @param nomProduit product name
     * @param scoreNutritionnel nutrition score from A to F
     * @param energie energy for 100g
     * @param matiereGrasse fat quantity for 100g
     * @param categorie normalized category
     * @param marque normalized brand
     */
    public Produit(
            String nomProduit,
            String scoreNutritionnel,
            BigDecimal energie,
            BigDecimal matiereGrasse,
            Categorie categorie,
            Marque marque) {
        this.nomProduit = nomProduit;
        this.scoreNutritionnel = scoreNutritionnel;
        this.energie = energie;
        this.matiereGrasse = matiereGrasse;
        this.categorie = categorie;
        this.marque = marque;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Produit produit)) {
            return false;
        }
        return id != null && id.equals(produit.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

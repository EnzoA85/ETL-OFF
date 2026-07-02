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
 * Product category imported from Open Food Facts.
 */
@Entity
@Table(name = "categorie")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Categorie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categorie")
    private Long id;

    @Column(name = "nom_categorie", nullable = false, unique = true)
    private String nomCategorie;

    /**
     * Creates a category with its unique business name.
     *
     * @param nomCategorie category name
     */
    public Categorie(String nomCategorie) {
        this.nomCategorie = nomCategorie;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Categorie categorie)) {
            return false;
        }
        return id != null && id.equals(categorie.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

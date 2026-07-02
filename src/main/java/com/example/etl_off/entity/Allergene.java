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
 * Unique allergen associated with products.
 */
@Entity
@Table(name = "allergene")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Allergene {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_allergene")
    private Long id;

    @Column(name = "nom_allergene", nullable = false, unique = true)
    private String nomAllergene;

    /**
     * Creates an allergen with its unique business name.
     *
     * @param nomAllergene allergen name
     */
    public Allergene(String nomAllergene) {
        this.nomAllergene = nomAllergene;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Allergene allergene)) {
            return false;
        }
        return id != null && id.equals(allergene.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

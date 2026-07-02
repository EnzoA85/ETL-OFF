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
 * Unique additive associated with products.
 */
@Entity
@Table(name = "additif")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Additif {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_additif")
    private Long id;

    @Column(name = "nom_additif", nullable = false, unique = true)
    private String nomAdditif;

    /**
     * Creates an additive with its unique business name.
     *
     * @param nomAdditif additive name
     */
    public Additif(String nomAdditif) {
        this.nomAdditif = nomAdditif;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Additif additif)) {
            return false;
        }
        return id != null && id.equals(additif.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

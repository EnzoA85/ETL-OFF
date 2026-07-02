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
 * Brand associated with one or more products.
 */
@Entity
@Table(name = "marque")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Marque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_marque")
    private Long id;

    @Column(name = "nom_marque", nullable = false, unique = true)
    private String nomMarque;

    /**
     * Creates a brand with its unique business name.
     *
     * @param nomMarque brand name
     */
    public Marque(String nomMarque) {
        this.nomMarque = nomMarque;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Marque marque)) {
            return false;
        }
        return id != null && id.equals(marque.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

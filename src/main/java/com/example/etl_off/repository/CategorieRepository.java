package com.example.etl_off.repository;

import com.example.etl_off.entity.Categorie;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data repository for categories.
 */
public interface CategorieRepository extends JpaRepository<Categorie, Long> {

    Optional<Categorie> findByNomCategorieIgnoreCase(String nomCategorie);

    boolean existsByNomCategorieIgnoreCase(String nomCategorie);
}

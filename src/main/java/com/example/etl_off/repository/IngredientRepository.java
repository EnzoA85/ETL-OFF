package com.example.etl_off.repository;

import com.example.etl_off.entity.Ingredient;
import com.example.etl_off.dto.TopItemResponse;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Spring Data repository for ingredients.
 */
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    Optional<Ingredient> findByNomIngredientIgnoreCase(String nomIngredient);

    boolean existsByNomIngredientIgnoreCase(String nomIngredient);

    @Query("""
            select new com.example.etl_off.dto.TopItemResponse(i.nomIngredient, count(p))
            from Produit p
            join p.ingredients i
            group by i.id, i.nomIngredient
            order by count(p) desc, i.nomIngredient
            """)
    List<TopItemResponse> findTopIngredients(Pageable pageable);
}

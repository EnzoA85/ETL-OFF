package com.example.etl_off.repository;

import com.example.etl_off.entity.Produit;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data repository for products.
 */
public interface ProduitRepository extends JpaRepository<Produit, Long> {

    @Query("""
            select p
            from Produit p
            join fetch p.categorie c
            join fetch p.marque m
            where lower(m.nomMarque) = lower(:brand)
            order by
                case
                    when p.scoreNutritionnel is null then 99
                    when upper(p.scoreNutritionnel) = 'A' then 1
                    when upper(p.scoreNutritionnel) = 'B' then 2
                    when upper(p.scoreNutritionnel) = 'C' then 3
                    when upper(p.scoreNutritionnel) = 'D' then 4
                    when upper(p.scoreNutritionnel) = 'E' then 5
                    when upper(p.scoreNutritionnel) = 'F' then 6
                    else 99
                end,
                p.nomProduit
            """)
    List<Produit> findTopByBrand(@Param("brand") String brand, Pageable pageable);

    @Query("""
            select p
            from Produit p
            join fetch p.categorie c
            join fetch p.marque m
            where lower(c.nomCategorie) = lower(:category)
            order by
                case
                    when p.scoreNutritionnel is null then 99
                    when upper(p.scoreNutritionnel) = 'A' then 1
                    when upper(p.scoreNutritionnel) = 'B' then 2
                    when upper(p.scoreNutritionnel) = 'C' then 3
                    when upper(p.scoreNutritionnel) = 'D' then 4
                    when upper(p.scoreNutritionnel) = 'E' then 5
                    when upper(p.scoreNutritionnel) = 'F' then 6
                    else 99
                end,
                p.nomProduit
            """)
    List<Produit> findTopByCategory(@Param("category") String category, Pageable pageable);

    @Query("""
            select p
            from Produit p
            join fetch p.categorie c
            join fetch p.marque m
            where lower(m.nomMarque) = lower(:brand)
              and lower(c.nomCategorie) = lower(:category)
            order by
                case
                    when p.scoreNutritionnel is null then 99
                    when upper(p.scoreNutritionnel) = 'A' then 1
                    when upper(p.scoreNutritionnel) = 'B' then 2
                    when upper(p.scoreNutritionnel) = 'C' then 3
                    when upper(p.scoreNutritionnel) = 'D' then 4
                    when upper(p.scoreNutritionnel) = 'E' then 5
                    when upper(p.scoreNutritionnel) = 'F' then 6
                    else 99
                end,
                p.nomProduit
            """)
    List<Produit> findTopByBrandAndCategory(
            @Param("brand") String brand,
            @Param("category") String category,
            Pageable pageable);
}

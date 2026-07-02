package com.example.etl_off.repository;

import com.example.etl_off.dto.TopItemResponse;
import com.example.etl_off.entity.Allergene;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Spring Data repository for allergens.
 */
public interface AllergeneRepository extends JpaRepository<Allergene, Long> {

    Optional<Allergene> findByNomAllergeneIgnoreCase(String nomAllergene);

    boolean existsByNomAllergeneIgnoreCase(String nomAllergene);

    @Query("""
            select new com.example.etl_off.dto.TopItemResponse(a.nomAllergene, count(p))
            from Produit p
            join p.allergenes a
            group by a.id, a.nomAllergene
            order by count(p) desc, a.nomAllergene
            """)
    List<TopItemResponse> findTopAllergenes(Pageable pageable);
}

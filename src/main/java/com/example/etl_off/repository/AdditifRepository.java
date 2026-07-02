package com.example.etl_off.repository;

import com.example.etl_off.dto.TopItemResponse;
import com.example.etl_off.entity.Additif;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Spring Data repository for additives.
 */
public interface AdditifRepository extends JpaRepository<Additif, Long> {

    Optional<Additif> findByNomAdditifIgnoreCase(String nomAdditif);

    boolean existsByNomAdditifIgnoreCase(String nomAdditif);

    @Query("""
            select new com.example.etl_off.dto.TopItemResponse(a.nomAdditif, count(p))
            from Produit p
            join p.additifs a
            group by a.id, a.nomAdditif
            order by count(p) desc, a.nomAdditif
            """)
    List<TopItemResponse> findTopAdditifs(Pageable pageable);
}

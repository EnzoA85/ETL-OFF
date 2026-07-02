package com.example.etl_off.repository;

import com.example.etl_off.entity.Marque;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data repository for brands.
 */
public interface MarqueRepository extends JpaRepository<Marque, Long> {

    Optional<Marque> findByNomMarqueIgnoreCase(String nomMarque);

    boolean existsByNomMarqueIgnoreCase(String nomMarque);
}

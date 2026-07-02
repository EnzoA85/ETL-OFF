package com.example.etl_off.dao;

import com.example.etl_off.entity.Produit;
import java.util.List;
import org.springframework.data.domain.Pageable;

/**
 * DAO dedicated to product persistence operations.
 */
public interface ProduitDao extends CrudDao<Produit> {

    List<Produit> findTopByBrand(String brand, Pageable pageable);

    List<Produit> findTopByCategory(String category, Pageable pageable);

    List<Produit> findTopByBrandAndCategory(String brand, String category, Pageable pageable);
}

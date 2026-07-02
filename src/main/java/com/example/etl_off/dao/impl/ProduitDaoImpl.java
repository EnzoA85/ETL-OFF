package com.example.etl_off.dao.impl;

import com.example.etl_off.dao.ProduitDao;
import com.example.etl_off.entity.Produit;
import com.example.etl_off.repository.ProduitRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/**
 * Spring Data based implementation of {@link ProduitDao}.
 */
@Repository
public class ProduitDaoImpl implements ProduitDao {

    private final ProduitRepository produitRepository;

    public ProduitDaoImpl(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }

    @Override
    public Produit save(Produit entity) {
        return produitRepository.save(entity);
    }

    @Override
    public List<Produit> saveAll(Iterable<Produit> entities) {
        return produitRepository.saveAll(entities);
    }

    @Override
    public Optional<Produit> findById(Long id) {
        return produitRepository.findById(id);
    }

    @Override
    public List<Produit> findAll() {
        return produitRepository.findAll();
    }

    @Override
    public List<Produit> findTopByBrand(String brand, Pageable pageable) {
        return produitRepository.findTopByBrand(brand, pageable);
    }

    @Override
    public List<Produit> findTopByCategory(String category, Pageable pageable) {
        return produitRepository.findTopByCategory(category, pageable);
    }

    @Override
    public List<Produit> findTopByBrandAndCategory(String brand, String category, Pageable pageable) {
        return produitRepository.findTopByBrandAndCategory(brand, category, pageable);
    }
}

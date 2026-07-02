package com.example.etl_off.dao.impl;

import com.example.etl_off.dao.CategorieDao;
import com.example.etl_off.entity.Categorie;
import com.example.etl_off.repository.CategorieRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * Spring Data based implementation of {@link CategorieDao}.
 */
@Repository
public class CategorieDaoImpl implements CategorieDao {

    private final CategorieRepository categorieRepository;

    public CategorieDaoImpl(CategorieRepository categorieRepository) {
        this.categorieRepository = categorieRepository;
    }

    @Override
    public Categorie save(Categorie entity) {
        return categorieRepository.save(entity);
    }

    @Override
    public List<Categorie> saveAll(Iterable<Categorie> entities) {
        return categorieRepository.saveAll(entities);
    }

    @Override
    public Optional<Categorie> findById(Long id) {
        return categorieRepository.findById(id);
    }

    @Override
    public List<Categorie> findAll() {
        return categorieRepository.findAll();
    }

    @Override
    public Optional<Categorie> findByNom(String nom) {
        return categorieRepository.findByNomCategorieIgnoreCase(nom);
    }

    @Override
    public boolean existsByNom(String nom) {
        return categorieRepository.existsByNomCategorieIgnoreCase(nom);
    }
}

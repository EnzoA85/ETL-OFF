package com.example.etl_off.dao.impl;

import com.example.etl_off.dao.MarqueDao;
import com.example.etl_off.entity.Marque;
import com.example.etl_off.repository.MarqueRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * Spring Data based implementation of {@link MarqueDao}.
 */
@Repository
public class MarqueDaoImpl implements MarqueDao {

    private final MarqueRepository marqueRepository;

    public MarqueDaoImpl(MarqueRepository marqueRepository) {
        this.marqueRepository = marqueRepository;
    }

    @Override
    public Marque save(Marque entity) {
        return marqueRepository.save(entity);
    }

    @Override
    public List<Marque> saveAll(Iterable<Marque> entities) {
        return marqueRepository.saveAll(entities);
    }

    @Override
    public Optional<Marque> findById(Long id) {
        return marqueRepository.findById(id);
    }

    @Override
    public List<Marque> findAll() {
        return marqueRepository.findAll();
    }

    @Override
    public Optional<Marque> findByNom(String nom) {
        return marqueRepository.findByNomMarqueIgnoreCase(nom);
    }

    @Override
    public boolean existsByNom(String nom) {
        return marqueRepository.existsByNomMarqueIgnoreCase(nom);
    }
}

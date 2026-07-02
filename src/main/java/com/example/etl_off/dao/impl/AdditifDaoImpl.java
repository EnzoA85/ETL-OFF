package com.example.etl_off.dao.impl;

import com.example.etl_off.dao.AdditifDao;
import com.example.etl_off.entity.Additif;
import com.example.etl_off.repository.AdditifRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * Spring Data based implementation of {@link AdditifDao}.
 */
@Repository
public class AdditifDaoImpl implements AdditifDao {

    private final AdditifRepository additifRepository;

    public AdditifDaoImpl(AdditifRepository additifRepository) {
        this.additifRepository = additifRepository;
    }

    @Override
    public Additif save(Additif entity) {
        return additifRepository.save(entity);
    }

    @Override
    public List<Additif> saveAll(Iterable<Additif> entities) {
        return additifRepository.saveAll(entities);
    }

    @Override
    public Optional<Additif> findById(Long id) {
        return additifRepository.findById(id);
    }

    @Override
    public List<Additif> findAll() {
        return additifRepository.findAll();
    }

    @Override
    public Optional<Additif> findByNom(String nom) {
        return additifRepository.findByNomAdditifIgnoreCase(nom);
    }

    @Override
    public boolean existsByNom(String nom) {
        return additifRepository.existsByNomAdditifIgnoreCase(nom);
    }
}

package com.example.etl_off.dao.impl;

import com.example.etl_off.dao.AllergeneDao;
import com.example.etl_off.entity.Allergene;
import com.example.etl_off.repository.AllergeneRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * Spring Data based implementation of {@link AllergeneDao}.
 */
@Repository
public class AllergeneDaoImpl implements AllergeneDao {

    private final AllergeneRepository allergeneRepository;

    public AllergeneDaoImpl(AllergeneRepository allergeneRepository) {
        this.allergeneRepository = allergeneRepository;
    }

    @Override
    public Allergene save(Allergene entity) {
        return allergeneRepository.save(entity);
    }

    @Override
    public List<Allergene> saveAll(Iterable<Allergene> entities) {
        return allergeneRepository.saveAll(entities);
    }

    @Override
    public Optional<Allergene> findById(Long id) {
        return allergeneRepository.findById(id);
    }

    @Override
    public List<Allergene> findAll() {
        return allergeneRepository.findAll();
    }

    @Override
    public Optional<Allergene> findByNom(String nom) {
        return allergeneRepository.findByNomAllergeneIgnoreCase(nom);
    }

    @Override
    public boolean existsByNom(String nom) {
        return allergeneRepository.existsByNomAllergeneIgnoreCase(nom);
    }
}

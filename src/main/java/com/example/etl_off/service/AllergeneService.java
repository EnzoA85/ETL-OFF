package com.example.etl_off.service;

import com.example.etl_off.dao.AllergeneDao;
import com.example.etl_off.entity.Allergene;
import org.springframework.stereotype.Service;

/**
 * Application service for allergens.
 */
@Service
public class AllergeneService extends AbstractNamedEntityService<Allergene> {

    public AllergeneService(AllergeneDao allergeneDao) {
        super(allergeneDao, Allergene::new);
    }
}

package com.example.etl_off.service;

import com.example.etl_off.dao.MarqueDao;
import com.example.etl_off.entity.Marque;
import org.springframework.stereotype.Service;

/**
 * Application service for brands.
 */
@Service
public class MarqueService extends AbstractNamedEntityService<Marque> {

    public MarqueService(MarqueDao marqueDao) {
        super(marqueDao, Marque::new);
    }
}

package com.example.etl_off.service;

import com.example.etl_off.dao.AdditifDao;
import com.example.etl_off.entity.Additif;
import org.springframework.stereotype.Service;

/**
 * Application service for additives.
 */
@Service
public class AdditifService extends AbstractNamedEntityService<Additif> {

    public AdditifService(AdditifDao additifDao) {
        super(additifDao, Additif::new);
    }
}

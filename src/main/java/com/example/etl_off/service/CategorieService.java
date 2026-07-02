package com.example.etl_off.service;

import com.example.etl_off.dao.CategorieDao;
import com.example.etl_off.entity.Categorie;
import org.springframework.stereotype.Service;

/**
 * Application service for categories.
 */
@Service
public class CategorieService extends AbstractNamedEntityService<Categorie> {

    public CategorieService(CategorieDao categorieDao) {
        super(categorieDao, Categorie::new);
    }
}

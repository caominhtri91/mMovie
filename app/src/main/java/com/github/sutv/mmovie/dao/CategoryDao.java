package com.github.sutv.mmovie.dao;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.github.sutv.mmovie.model.Category;
import com.github.sutv.mmovie.model.Category_Relation;
import com.github.sutv.mmovie.model.OrmaDatabase;

@Singleton
public class CategoryDao {

    OrmaDatabase orma;

    @Inject
    public CategoryDao(OrmaDatabase orma) {
        this.orma = orma;
    }

    private Category_Relation categoryRelation() {
        return orma.relationOfCategory();
    }

    public List<Category> findAll() {
        return categoryRelation().selector().toList();
    }

}

package com.github.sutv.mmovie.dao;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.github.sutv.mmovie.model.OrmaDatabase;
import com.github.sutv.mmovie.model.Place;
import com.github.sutv.mmovie.model.Place_Relation;

@Singleton
public class PlaceDao {

    OrmaDatabase orma;

    @Inject
    public PlaceDao(OrmaDatabase orma) {
        this.orma = orma;
    }

    private Place_Relation placeRelation() {
        return orma.relationOfPlace();
    }

    public List<Place> findAll() {
        return placeRelation().selector().toList();
    }

}

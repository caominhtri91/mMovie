package com.github.sutv.mmovie.dao;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.github.sutv.mmovie.model.Contributor;
import com.github.sutv.mmovie.model.Contributor_Relation;
import com.github.sutv.mmovie.model.OrmaDatabase;

@Singleton
public class ContributorDao {

    OrmaDatabase orma;

    @Inject
    public ContributorDao(OrmaDatabase orma) {
        this.orma = orma;
    }

    private Contributor_Relation contributorRelation() {
        return orma.relationOfContributor();
    }

    public List<Contributor> findAll() {
        return contributorRelation().selector().toList();
    }

    public void upserterAll(@NonNull List<Contributor> contributors) {
        orma.transactionAsync(() -> contributorRelation().upserter().executeAll(contributors)).subscribe();
    }
}

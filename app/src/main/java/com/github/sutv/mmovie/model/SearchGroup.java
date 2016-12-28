package com.github.sutv.mmovie.model;

public interface SearchGroup {

    int getId();

    String getName();

    Type getType();

    enum Type {CATEGORY, PLACE, TITLE}

}

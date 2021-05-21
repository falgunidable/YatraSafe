package com.example.finalproject.model;

import java.io.Serializable;

public final class StatesCities implements Serializable {
    private String image;
    private String name;

    public StatesCities() {
    }

    public StatesCities(String name,String image) {
        this.name = name;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }
}

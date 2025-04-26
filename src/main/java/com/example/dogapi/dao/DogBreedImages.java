package com.example.dogapi.dao;

import java.util.ArrayList;
import java.util.List;

public class DogBreedImages {
    private String name;
    private List<String> images = new ArrayList<>();

    public DogBreedImages() {
    }

    public DogBreedImages(String name, List<String> images) {
        this.name = name;
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}

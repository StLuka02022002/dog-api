package com.example.dogapi.dao;

public class SubBreed {
    private String breed;
    private String subBreed;

    public SubBreed() {
    }

    public SubBreed(String breed, String subBreed) {
        this.breed = breed;
        this.subBreed = subBreed;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getSubBreed() {
        return subBreed;
    }

    public void setSubBreed(String subBreed) {
        this.subBreed = subBreed;
    }
}

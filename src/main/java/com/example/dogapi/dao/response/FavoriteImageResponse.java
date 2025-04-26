package com.example.dogapi.dao.response;

import com.example.dogapi.dao.DogBreedImages;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FavoriteImageResponse {
    private List<DogBreedImages> breeds = new ArrayList<>();

    public FavoriteImageResponse() {
    }

    public FavoriteImageResponse(List<DogBreedImages> breeds) {
        this.breeds = breeds;
    }

    public List<DogBreedImages> getBreeds() {
        return breeds;
    }

    public void setBreeds(List<DogBreedImages> breeds) {
        this.breeds = breeds;
    }

    public static FavoriteImageResponse of(Map<String, List<String>> breeds) {
        List<DogBreedImages> breedImages = breeds.entrySet().stream()
                .map(entry -> new DogBreedImages(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        return new FavoriteImageResponse(breedImages);
    }
}

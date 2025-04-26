package com.example.dogapi.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DogBreeds {
    private Map<String, List<SubBreed>> breeds = new HashMap<>();

    public DogBreeds() {
    }

    public DogBreeds(Map<String, List<SubBreed>> breeds) {
        this.breeds = breeds;
    }

    public Map<String, List<SubBreed>> getBreeds() {
        return breeds;
    }

    public void setBreeds(Map<String, List<SubBreed>> breeds) {
        this.breeds = breeds;
    }

    public static DogBreeds of(Map<String, List<String>> breeds) {
        Map<String, List<SubBreed>> subBreeds = new HashMap<>();

        for (Map.Entry<String, List<String>> entry : breeds.entrySet()) {
            String breedName = entry.getKey();
            List<String> subBreedNames = entry.getValue();

            List<SubBreed> listSubBreeds = subBreedNames.stream()
                    .map(subBreedName -> new SubBreed(breedName, subBreedName))
                    .collect(Collectors.toList());

            if (listSubBreeds.isEmpty()) {
                listSubBreeds.add(new SubBreed(breedName, null));
                //listSubBreeds.add(new SubBreed(breedName, breedName)); //На усмотрение
            }

            subBreeds.put(breedName, listSubBreeds);
        }

        return new DogBreeds(subBreeds);
    }
}

package com.example.dogapi.dao.response;

import java.util.Set;

public class DogBreedsResponse {
    private final Set<String> breeds;

    public DogBreedsResponse(Set<String> breeds) {
        this.breeds = breeds;
    }

    public Set<String> getBreeds() {
        return breeds;
    }
}

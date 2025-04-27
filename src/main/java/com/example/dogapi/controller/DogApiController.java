package com.example.dogapi.controller;

import com.example.dogapi.client.DogApiClient;
import com.example.dogapi.dao.request.FavoriteImageRequest;
import com.example.dogapi.dao.response.DogBreedsResponse;
import com.example.dogapi.dao.response.DogImageResponse;
import com.example.dogapi.dao.response.FavoriteImageResponse;
import com.example.dogapi.entity.FavoriteImage;
import com.example.dogapi.exception.DogApiException;
import com.example.dogapi.service.FavoriteImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/dogs")
public class DogApiController {
    private final FavoriteImageService service;
    private final DogApiClient dogApiClient;

    public DogApiController(FavoriteImageService service, DogApiClient dogApiClient) {
        this.service = service;
        this.dogApiClient = dogApiClient;
    }

    @GetMapping("/breeds")
    public ResponseEntity<DogBreedsResponse> getAllBreads() {
        return ResponseEntity.ok(new DogBreedsResponse(dogApiClient.getAllDogBreeds()
                .getBreeds()
                .keySet()));
    }


    @GetMapping("/{breed}/random")
    public ResponseEntity<DogImageResponse> getRandomImage(@PathVariable(required = false) String breed) {
        if (breed == null) {
            throw new DogApiException("Необходимо ввести breed в пути запроса");
        }
        return ResponseEntity.ok(dogApiClient.getRandomImage(breed));
    }

    @PostMapping("/favorite")
    public ResponseEntity<Void> addFavoriteImage(@RequestBody FavoriteImageRequest request) {
        if (request.getUsername() == null
                || request.getImage_url() == null
                || request.getBreed() == null) {
            throw new DogApiException("Тело запроса должно содержать все поля.");
        }

        service.addFavoriteImage(request.getUsername(),
                request.getImage_url(),
                request.getBreed());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{username}/favorite")
    public ResponseEntity<FavoriteImageResponse> getFavoriteImage(@PathVariable String username) {
        Map<String, List<String>> breeds = service.getUserWithFavoriteImages(username)
                .getImages()
                .stream()
                .collect(Collectors.groupingBy(
                        FavoriteImage::getBreed,
                        Collectors.mapping(
                                FavoriteImage::getImageUrl,
                                Collectors.toList()
                        )
                ));
        return ResponseEntity.ok(FavoriteImageResponse.of(breeds));
    }
}

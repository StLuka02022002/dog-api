package com.example.dogapi.controller;

import com.example.dogapi.client.DogApiClient;
import com.example.dogapi.dao.DogBreedImages;
import com.example.dogapi.dao.DogBreeds;
import com.example.dogapi.dao.request.FavoriteImageRequest;
import com.example.dogapi.dao.response.DogBreedsResponse;
import com.example.dogapi.dao.response.DogImageResponse;
import com.example.dogapi.dao.response.FavoriteImageResponse;
import com.example.dogapi.entity.FavoriteImage;
import com.example.dogapi.entity.User;
import com.example.dogapi.exception.DogApiException;
import com.example.dogapi.service.FavoriteImageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DogApiControllerTest {

    @Mock
    private FavoriteImageService favoriteImageService;

    @Mock
    private DogApiClient dogApiClient;

    @InjectMocks
    private DogApiController dogApiController;

    @Test
    void getAllBreedTest() {
        Map<String, List<String>> breeds = Map.of(
                "labrador", List.of(),
                "poodle", List.of("toy", "miniature")
        );
        when(dogApiClient.getAllDogBreeds()).thenReturn(DogBreeds.of(breeds));

        ResponseEntity<DogBreedsResponse> response = dogApiController.getAllBreads();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(Set.of("labrador", "poodle"), response.getBody().getBreeds());

        verify(dogApiClient).getAllDogBreeds();
    }

    @Test
    void getRandomImageTest() {
        String breed = "labrador";

        DogImageResponse dogImageResponse = new DogImageResponse("https://api/dogs/labrador.jpg");
        when(dogApiClient.getRandomImage(breed)).thenReturn(dogImageResponse);

        ResponseEntity<DogImageResponse> response = dogApiController.getRandomImage(breed);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dogImageResponse, response.getBody());

        verify(dogApiClient).getRandomImage(breed);
    }

    @Test
    void getRandomImageWithNullBreedTest() {
        DogApiException exception = assertThrows(DogApiException.class,
                () -> dogApiController.getRandomImage(null));
        assertEquals("Необходимо ввести breed в пути запроса", exception.getMessage());

        verifyNoInteractions(dogApiClient);
    }

    @Test
    void addFavoriteImageTest() {
        FavoriteImageRequest request = new FavoriteImageRequest(
                "username", "https://api/dogs/dog.jpg", "labrador");

        ResponseEntity<Void> response = dogApiController.addFavoriteImage(request);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        verify(favoriteImageService).addFavoriteImage(
                request.getUsername(),
                request.getImage_url(),
                request.getBreed());
    }

    @Test
    void addFavoriteImageWithInvalidRequestTest() {
        FavoriteImageRequest invalidRequest = new FavoriteImageRequest(null, null, null);
        assertThrows(DogApiException.class, () -> dogApiController.addFavoriteImage(invalidRequest));

        verifyNoInteractions(favoriteImageService);
    }

    @Test
    void getFavoriteImagesTest() {
        String username = "username";
        FavoriteImage image1 = new FavoriteImage("https://api/dogs/1.jpg", "labrador");
        FavoriteImage image2 = new FavoriteImage("https://api/dogs/2.jpg", "poodle");
        FavoriteImage image3 = new FavoriteImage("https://api/dogs/3.jpg", "labrador");

        User user = new User(username);
        user.setImages(List.of(image1, image2, image3));
        when(favoriteImageService.getUserWithFavoriteImages(username))
                .thenReturn(user);

        ResponseEntity<FavoriteImageResponse> response =
                dogApiController.getFavoriteImage(username);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        List<DogBreedImages> breeds = response.getBody().getBreeds();
        assertEquals(2, breeds.size());

        Optional<List<String>> list1 = breeds.stream()
                .filter(f -> f.getName().equals("labrador"))
                .map(DogBreedImages::getImages)
                .findAny();
        Optional<List<String>> list2 = breeds.stream()
                .filter(f -> f.getName().equals("poodle"))
                .map(DogBreedImages::getImages)
                .findAny();

        assertTrue(list1.isPresent());
        assertTrue(list2.isPresent());
        assertEquals(List.of("https://api/dogs/1.jpg", "https://api/dogs/3.jpg"), list1.get());
        assertEquals(List.of("https://api/dogs/2.jpg"), list2.get());
        verify(favoriteImageService).getUserWithFavoriteImages(username);
    }

}
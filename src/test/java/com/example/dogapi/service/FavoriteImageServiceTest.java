package com.example.dogapi.service;

import com.example.dogapi.entity.FavoriteImage;
import com.example.dogapi.entity.User;
import com.example.dogapi.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavoriteImageServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FavoriteImageService favoriteImageService;

    @Test
    void addFavoriteImageTest() {
        String username = "username";
        String imageUrl = "https://api/dogs/dog.jpg";
        String breed = "labrador";

        User user = new User(username);
        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(user));

        favoriteImageService.addFavoriteImage(username, imageUrl, breed);

        verify(userRepository).findByUsername(username);
        verify(userRepository).save(user);
        assertEquals(1, user.getImages().size());

        FavoriteImage addedImage = user.getImages().get(0);
        assertEquals(imageUrl, addedImage.getImageUrl());
        assertEquals(breed, addedImage.getBreed());
    }

    @Test
    void addFavoriteImageWhenNotExistsTest() {
        String username = "username";
        String imageUrl = "https://api/dogs/dog.jpg";
        String breed = "poodle";

        when(userRepository.findByUsername(username))
                .thenReturn(Optional.empty());
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        favoriteImageService.addFavoriteImage(username, imageUrl, breed);

        verify(userRepository).findByUsername(username);
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User user = userCaptor.getValue();
        assertEquals(username, user.getUsername());
        assertEquals(1, user.getImages().size());
        FavoriteImage addedImage = user.getImages().get(0);
        assertEquals(imageUrl, addedImage.getImageUrl());
        assertEquals(breed, addedImage.getBreed());
    }

    @Test
    void getUserWithFavoriteImagesTest() {
        String username = "username";
        User user = new User(username);
        user.add(new FavoriteImage("url", "breed"));

        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(user));

        User result = favoriteImageService.getUserWithFavoriteImages(username);

        assertEquals(user, result);
        verify(userRepository).findByUsername(username);
    }

    @Test
    void getUserWithFavoriteImagesWhenUserNotFoundTest() {
        String username = "username";
        when(userRepository.findByUsername(username))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> favoriteImageService.getUserWithFavoriteImages(username)
        );

        verify(userRepository).findByUsername(username);
    }

    @Test
    void getUserWithFavoriteImagesWithEmptyImagesTest() {
        String username = "username";
        User user = new User(username);

        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(user));

        User result = favoriteImageService.getUserWithFavoriteImages(username);

        assertNotNull(result);
        assertTrue(result.getImages().isEmpty());
    }
}
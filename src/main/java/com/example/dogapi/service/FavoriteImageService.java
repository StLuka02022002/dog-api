package com.example.dogapi.service;

import com.example.dogapi.entity.FavoriteImage;
import com.example.dogapi.entity.User;
import com.example.dogapi.repository.FavoriteImageRepository;
import com.example.dogapi.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FavoriteImageService {

    private final UserRepository userRepository;
    private final FavoriteImageRepository favoriteImageRepository;

    public FavoriteImageService(UserRepository userRepository,
                                FavoriteImageRepository favoriteImageRepository) {
        this.userRepository = userRepository;
        this.favoriteImageRepository = favoriteImageRepository;
    }

    public void addFavoriteImage(String username, String imageUrl, String breed) {// Возможно парсить url
        User user = new User(username);
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            user = userOptional.get();
        }
        user.add(new FavoriteImage(imageUrl, breed));
        userRepository.save(user);
    }

    public User getUserWithFavoriteImages(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new EntityNotFoundException("Пользователь с username=%s не найден"
                    .formatted(username));
        }
        return userOptional.get();
    }
}

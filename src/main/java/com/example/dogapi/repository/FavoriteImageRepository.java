package com.example.dogapi.repository;

import com.example.dogapi.entity.FavoriteImage;
import com.example.dogapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteImageRepository extends JpaRepository<FavoriteImage, Long> {

    List<FavoriteImage> findByUser(User user);
}

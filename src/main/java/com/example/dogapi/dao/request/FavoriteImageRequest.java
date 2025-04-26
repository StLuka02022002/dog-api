package com.example.dogapi.dao.request;

public class FavoriteImageRequest {
    private String username;
    private String image_url;
    private String breed;

    public FavoriteImageRequest() {
    }

    public FavoriteImageRequest(String username, String image_url, String breed) {
        this.username = username;
        this.image_url = image_url;
        this.breed = breed;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }
}

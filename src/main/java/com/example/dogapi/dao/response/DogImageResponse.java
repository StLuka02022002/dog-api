package com.example.dogapi.dao.response;

public class DogImageResponse {
    private String image_url;

    public DogImageResponse() {
    }

    public DogImageResponse(String image_url) {
        this.image_url = image_url;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

}

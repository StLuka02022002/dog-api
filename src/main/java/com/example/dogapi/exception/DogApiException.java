package com.example.dogapi.exception;

public class DogApiException extends RuntimeException{
    public DogApiException() {
    }

    public DogApiException(String message) {
        super(message);
    }
}

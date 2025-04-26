package com.example.dogapi.dao;

public class ErrorMessage {
    private final String status = "error";
    private final String message;

    public ErrorMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}

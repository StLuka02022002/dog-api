package com.example.dogapi.handler;

import com.example.dogapi.dao.ErrorMessage;
import com.example.dogapi.exception.DogApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DogApiException.class)
    public ResponseEntity<ErrorMessage> handleDogApiClientException(DogApiException e, WebRequest request) {
        return new ResponseEntity<>(new ErrorMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleGeneralException(Exception e, WebRequest request) {
        ErrorMessage error = new ErrorMessage("Произошла ошибка: " + e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

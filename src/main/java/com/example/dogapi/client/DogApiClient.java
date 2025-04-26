package com.example.dogapi.client;

import com.example.dogapi.dao.DogBreeds;
import com.example.dogapi.dao.response.DogApiClientResponse;
import com.example.dogapi.dao.response.DogImageResponse;
import com.example.dogapi.exception.DogApiClientException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class DogApiClient {

    public final static String SUCCESS_STATUS = "success";
    public final static String ERROR_STATUS = "error";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${dogapi.url.allbreeds}")
    private String urlAllBreads;

    @Value("${dogapi.url.randomimage}")
    private String urlRandomImage;

    public DogApiClient(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public DogBreeds getAllDogBreeds() throws DogApiClientException {
        String response = getResponse(urlAllBreads);
        try {
            Map<String, List<String>> breeds = objectMapper.readValue(response,
                    new TypeReference<>() {
                    });
            return DogBreeds.of(breeds);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public DogImageResponse getRandomImage(String breed) throws DogApiClientException {
        String response = getResponse(urlRandomImage.formatted(breed));
        return new DogImageResponse(response);
    }

    public String getResponse(String url) {
        try {
            ResponseEntity<DogApiClientResponse> response =
                    restTemplate.getForEntity(url, DogApiClientResponse.class);
            validateResponse(response);
            return Objects.requireNonNull(response.getBody()).getMessage();
        } catch (RestClientException e) {
            throw new DogApiClientException("Неудачный запрос к серверу. Ошибка: %s"
                    .formatted(e.getMessage()));
        }
    }

    private void validateResponse(ResponseEntity<DogApiClientResponse> response) {
        if (response.getBody() == null) {
            throw new DogApiClientException("Неудачный запрос к серверу. Пустое тело ответа.");
        }

        String status = response.getBody().getStatus();

        if (status.equalsIgnoreCase(ERROR_STATUS)) {
            throw new DogApiClientException("Неудачный запрос к серверу. Ошибка: %s"
                    .formatted(response.getBody().getMessage()));
        }

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new DogApiClientException("Неудачный запрос к серверу. Статус: %s."
                    .formatted(response.getStatusCode()));
        }

        if (!status.equalsIgnoreCase(SUCCESS_STATUS)) {
            throw new DogApiClientException("Неудачный запрос к серверу. Ошибка: %s"
                    .formatted(response.getBody().getMessage()));
        }
    }
}

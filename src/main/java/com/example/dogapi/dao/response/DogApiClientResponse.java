package com.example.dogapi.dao.response;

import com.example.dogapi.client.MessageDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DogApiClientResponse {

    @JsonDeserialize(using = MessageDeserializer.class)
    private String message;
    private String status;
    private Integer code;

    private final Map<String, Object> dynamicProperties = new HashMap<>();

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Map<String, Object> getDynamicProperties() {
        return dynamicProperties;
    }
}

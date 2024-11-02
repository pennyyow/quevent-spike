package com.quevent.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegistrationResponse {
    private String message;

    public RegistrationResponse(String message) {
        this.message = message;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }
}

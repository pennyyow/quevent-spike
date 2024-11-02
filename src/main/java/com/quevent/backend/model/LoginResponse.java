package com.quevent.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginResponse {
    private Long userId;
    private String accessToken;
    private String refreshToken;

    public LoginResponse(Long userId, String accessToken, String refreshToken) {
        this.userId = userId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    // @JsonPropery annotation were added because of 406 NOT_ACCEPTABLE error (in POSTMAN it shows 401/403 error, in logs its 406 error)
    @JsonProperty("user_id")
    public Long getUserId() {
        return userId;
    }

    @JsonProperty("access_token")
    public String getAccessToken() {
        return accessToken;
    }

    @JsonProperty("refresh_token")
    public String getRefreshToken() {
        return refreshToken;
    }
}

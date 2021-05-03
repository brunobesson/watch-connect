package org.camptocamp.watchconnect.strava.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StravaToken {
    private final String accessToken;
    private final String refreshToken;
    private final Integer expiresAt;
    private final Integer expiresIn;

    @JsonCreator
    public StravaToken(
            @JsonProperty("access_token") final String accessToken,
            @JsonProperty("refresh_token") final String refreshToken,
            @JsonProperty("expires_at") final Integer expiresAt,
            @JsonProperty("expires_in") final Integer expiresIn
    ) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresAt = expiresAt;
        this.expiresIn = expiresIn;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Integer getExpiresAt() {
        return expiresAt;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }
}

package org.camptocamp.watchconnect;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StravaToken {
    private final String accessToken;
    private final String refreshToken;
    private final String expiresAt;

    @JsonCreator
    public StravaToken(
            @JsonProperty("access_token") final String accessToken,
            @JsonProperty("refresh_token") final String refreshToken,
            @JsonProperty("expires_at") final String expiresAt
    ) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresAt = expiresAt;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getExpiresAt() {
        return expiresAt;
    }
}

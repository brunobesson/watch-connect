package org.camptocamp.watchconnect;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StravaSubscriptionValidation {
    private final String challenge;

    public StravaSubscriptionValidation(final String challenge) {
        this.challenge = challenge;
    }

    @JsonProperty("hub.challenge")
    public String getChallenge() {
        return challenge;
    }
}

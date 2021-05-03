package org.camptocamp.watchconnect;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;

public class StravaSubscription {
    private final String id;
    private final String applicationId;
    private final String callbackUrl;
    private final ZonedDateTime createdAt;
    private final ZonedDateTime updatedAt;

    @JsonCreator
    public StravaSubscription(
            @JsonProperty("id") final String id,
            @JsonProperty("application_id") final String applicationId,
            @JsonProperty("callback_url") final String callbackUrl,
            @JsonProperty("created_at") final ZonedDateTime createdAt,
            @JsonProperty("updated_at") final ZonedDateTime updatedAt
    ) {
        this.id = id;
        this.applicationId = applicationId;
        this.callbackUrl = callbackUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }
}

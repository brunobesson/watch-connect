package org.camptocamp.watchconnect.strava.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.camptocamp.watchconnect.config.ZonedDateTimeDeserializer;

import java.time.ZonedDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
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
            @JsonProperty("created_at") @JsonDeserialize(using = ZonedDateTimeDeserializer.class) final ZonedDateTime createdAt,
            @JsonProperty("updated_at") @JsonDeserialize(using = ZonedDateTimeDeserializer.class) final ZonedDateTime updatedAt
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

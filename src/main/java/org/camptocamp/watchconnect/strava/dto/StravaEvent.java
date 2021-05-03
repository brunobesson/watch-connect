package org.camptocamp.watchconnect.strava.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class StravaEvent {
    private final StravaEventType eventType;
    private final String id;
    private final StravaEventAspect aspect;
    private final Map<String, String> updated;
    private final String owner;
    private final String subscriptionId;
    private final Long date;

    @JsonCreator
    public StravaEvent(
            @JsonProperty("object_type") final StravaEventType eventType,
            @JsonProperty("object_id") final String id,
            @JsonProperty("aspect_type") final StravaEventAspect aspect,
            @JsonProperty("updates") Map<String, String> updated,
            @JsonProperty("owner_id") final String owner,
            @JsonProperty("subscription_id") final String subscriptionId,
            @JsonProperty("event_time") final Long date
    ) {
        this.eventType = eventType;
        this.id = id;
        this.aspect = aspect;
        this.updated = updated;
        this.owner = owner;
        this.subscriptionId = subscriptionId;
        this.date = date;
    }

    public StravaEventType getEventType() {
        return eventType;
    }

    public String getId() {
        return id;
    }

    public StravaEventAspect getAspect() {
        return aspect;
    }

    public Map<String, String> getUpdated() {
        return updated;
    }

    public String getOwner() {
        return owner;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public Long getDate() {
        return date;
    }
}

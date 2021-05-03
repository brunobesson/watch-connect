package org.camptocamp.watchconnect.strava.dto;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Locale;

public enum StravaEventAspect {
    CREATE,
    UPDATE,
    DELETE;

    @JsonValue
    public String value() {
        return name().toLowerCase(Locale.ROOT);
    }
}

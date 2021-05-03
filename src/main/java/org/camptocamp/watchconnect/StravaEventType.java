package org.camptocamp.watchconnect;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Locale;

public enum StravaEventType {
    ACTIVITY,
    ATHLETE;

    @JsonValue
    public String value() {
        return name().toLowerCase(Locale.ROOT);
    }
}

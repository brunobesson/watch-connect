package org.camptocamp.watchconnect.strava.dto;

import java.util.Arrays;

public enum Scope {
    READ("read"),
    ACTIVITY_READ("activity:read"),
    ACTIVITY_READ_ALL("activity:read_all");

    private final String code;

    Scope(final String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }

    public static Scope of(final String code) {
        return Arrays.stream(values()).filter(scope -> scope.code().equals(code)).findFirst().orElse(null);
    }
}

package org.camptocamp.watchconnect.strava.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SummaryActivity {

    private final String id;
    private final String name;
    private final ActivityType activityType;
    private final String startDate;
    private final List<Float> startCoordinates;
    private final PolylineMap map;

    @JsonCreator
    public SummaryActivity(
            @JsonProperty("id") final String id,
            @JsonProperty("name") final String name,
            @JsonProperty("type") final ActivityType activityType,
            @JsonProperty("start_date_local") final String startDate,
            @JsonProperty("start_latlng") final List<Float> startCoordinates,
            @JsonProperty("map") final PolylineMap map
    ) {
        this.id = id;
        this.name = name;
        this.activityType = activityType;
        this.startDate = startDate;
        this.startCoordinates = startCoordinates;
        this.map = map;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStartDate() {
        return startDate;
    }

    public PolylineMap getMap() {
        return map;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public List<Float> getStartCoordinates() {
        return startCoordinates;
    }
}

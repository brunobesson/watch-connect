package org.camptocamp.watchconnect;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PolylineMap {
    private final String polyline;
    private final String summaryPolyline;

    @JsonCreator
    public PolylineMap(
            @JsonProperty("polyline") final String polyline,
            @JsonProperty("summary_polyline") final String summaryPolyline
    ) {
        this.polyline = polyline;
        this.summaryPolyline = summaryPolyline;
    }

    public String getPolyline() {
        return polyline;
    }

    public String getSummaryPolyline() {
        return summaryPolyline;
    }
}

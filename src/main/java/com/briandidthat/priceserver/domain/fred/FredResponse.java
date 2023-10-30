package com.briandidthat.priceserver.domain.fred;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class FredResponse {
    @JsonProperty("observation_start")
    private String observationStart;
    @JsonProperty("observation_end")
    private String observationEnd;
    @JsonProperty("count")
    private Integer count;
    @JsonProperty("observations")
    private List<Observation> observations;

    public FredResponse() {}

    public FredResponse(String observationStart, String observationEnd, Integer count, List<Observation> observations) {
        this.observationStart = observationStart;
        this.observationEnd = observationEnd;
        this.count = count;
        this.observations = observations;
    }

    public String getObservationStart() {
        return observationStart;
    }

    public String getObservationEnd() {
        return observationEnd;
    }

    public Integer getCount() {
        return count;
    }

    public List<Observation> getObservations() {
        return observations;
    }
}

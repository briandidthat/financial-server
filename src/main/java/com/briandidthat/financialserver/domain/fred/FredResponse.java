package com.briandidthat.financialserver.domain.fred;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FredResponse that = (FredResponse) o;
        return Objects.equals(observationStart, that.observationStart) && Objects.equals(observationEnd, that.observationEnd)
                && Objects.equals(count, that.count) && Objects.equals(observations, that.observations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(observationStart, observationEnd, count, observations);
    }

    @Override
    public String toString() {
        return "FredResponse{" +
                "observationStart='" + observationStart + '\'' +
                ", observationEnd='" + observationEnd + '\'' +
                ", count=" + count +
                ", observations=" + observations +
                '}';
    }
}

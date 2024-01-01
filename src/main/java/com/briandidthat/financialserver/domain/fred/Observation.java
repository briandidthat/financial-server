package com.briandidthat.financialserver.domain.fred;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Observation (@JsonProperty("realtime_start") String realTimeStart,
                           @JsonProperty("realtime_end") String realTimeEnd,
                           @JsonProperty("date") String date,
                           @JsonProperty("value") String value) implements Serializable {
}

package com.briandidthat.econserver.domain.twelve;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class TimeSeriesResponse {
    private Map<String, String> meta;
    private List<Value> values;
    private String status;

    public TimeSeriesResponse() {
    }

    public TimeSeriesResponse(Map<String, String> meta, List<Value> values, String status) {
        this.meta = meta;
        this.values = values;
        this.status = status;
    }

    public Map<String, String> getMeta() {
        return meta;
    }

    public void setMeta(Map<String, String> meta) {
        this.meta = meta;
    }

    public List<Value> getValues() {
        return values;
    }

    public void setValues(List<Value> values) {
        this.values = values;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class Value {
        LocalDate datetime;
        String open;
        String high;
        String low;
        String close;
        String volume;

        public Value(LocalDate datetime, String open, String high, String low, String close, String volume) {
            this.datetime = datetime;
            this.open = open;
            this.high = high;
            this.low = low;
            this.close = close;
            this.volume = volume;
        }

        public LocalDate getDatetime() {
            return datetime;
        }

        public String getOpen() {
            return open;
        }

        public String getHigh() {
            return high;
        }

        public String getLow() {
            return low;
        }

        public String getClose() {
            return close;
        }

        public String getVolume() {
            return volume;
        }
    }
}

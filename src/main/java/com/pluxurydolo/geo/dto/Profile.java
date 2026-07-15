package com.pluxurydolo.geo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import static java.util.Arrays.stream;

public enum Profile {
    @JsonProperty("bike") BIKE("bike", "bc"),
    @JsonProperty("foot") FOOT("foot", "pd");

    private final String type;
    private final String rtt;

    Profile(String type, String rtt) {
        this.type = type;
        this.rtt = rtt;
    }

    public String getRtt() {
        return rtt;
    }

    public static Profile fromType(String type) {
        return stream(values())
            .filter(value -> value.getType().equals(type))
            .findFirst()
            .orElse(null);
    }

    private String getType() {
        return type;
    }
}

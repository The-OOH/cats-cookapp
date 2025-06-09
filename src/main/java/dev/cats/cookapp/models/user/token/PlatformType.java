package dev.cats.cookapp.models.user.token;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PlatformType {
    ANDROID("android"),
    IOS("ios");

    private final String value;

    PlatformType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static PlatformType fromValue(String v) {
        for (PlatformType pt : values()) {
            if (pt.value.equalsIgnoreCase(v)) {
                return pt;
            }
        }
        throw new IllegalArgumentException("Unknown PlatformType: " + v);
    }
}

package dev.cats.cookapp.models.user.token;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PlatformType {
    ANDROID("android"),
    IOS("ios");

    private final String value;

    PlatformType(final String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return this.value;
    }

    @JsonCreator
    public static PlatformType fromValue(final String v) {
        for (final PlatformType pt : PlatformType.values()) {
            if (pt.value.equalsIgnoreCase(v)) {
                return pt;
            }
        }
        throw new IllegalArgumentException("Unknown PlatformType: " + v);
    }
}

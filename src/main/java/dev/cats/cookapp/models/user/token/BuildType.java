package dev.cats.cookapp.models.user.token;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum BuildType {
    EXPO("expo"),
    FCM("fcm"),
    apns("apns");

    private final String value;

    BuildType(final String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return this.value;
    }

    @JsonCreator
    public static BuildType fromValue(final String v) {
        for (final BuildType pt : BuildType.values()) {
            if (pt.value.equalsIgnoreCase(v)) {
                return pt;
            }
        }
        throw new IllegalArgumentException("Unknown PlatformType: " + v);
    }
}

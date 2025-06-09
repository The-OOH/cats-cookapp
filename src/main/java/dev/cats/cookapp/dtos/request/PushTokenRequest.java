package dev.cats.cookapp.dtos.request;

import dev.cats.cookapp.models.user.token.BuildType;
import dev.cats.cookapp.models.user.token.PlatformType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PushTokenRequest {
    String token;
    PlatformType platform;
    BuildType Type;
}

package dev.cats.cookapp.services;

import dev.cats.cookapp.dtos.request.PushTokenRequest;
import dev.cats.cookapp.dtos.response.SavePushTokenResponse;

public interface UserPushTokenService {
    SavePushTokenResponse savePushToken(String userId, PushTokenRequest request);
}

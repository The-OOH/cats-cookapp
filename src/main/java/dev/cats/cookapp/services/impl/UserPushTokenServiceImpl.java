package dev.cats.cookapp.services.impl;

import dev.cats.cookapp.dtos.request.PushTokenRequest;
import dev.cats.cookapp.dtos.response.SavePushTokenResponse;
import dev.cats.cookapp.repositories.UserPushTokenRepository;
import dev.cats.cookapp.services.ClerkService;
import dev.cats.cookapp.services.UserPushTokenService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserPushTokenServiceImpl implements UserPushTokenService {
    UserPushTokenRepository userPushTokenRepository;
    ClerkService clerkService;

    @Override
    public SavePushTokenResponse savePushToken(final String userId, final PushTokenRequest request) {
        if (this.clerkService.getUserDetailsById(userId).isEmpty()) {
            return new SavePushTokenResponse(false, "User not found");
        }

        this.userPushTokenRepository.upsert(userId,
                request.getToken(),
                request.getType().name(),
                request.getPlatform().name());

        return new SavePushTokenResponse(true, "Push token stored successfully");
    }
}

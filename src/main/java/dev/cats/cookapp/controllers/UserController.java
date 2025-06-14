package dev.cats.cookapp.controllers;

import dev.cats.cookapp.dtos.request.PushTokenRequest;
import dev.cats.cookapp.dtos.response.SavePushTokenResponse;
import dev.cats.cookapp.services.UserPushTokenService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserPushTokenService userPushTokenService;

    @PutMapping("/push-token")
    public ResponseEntity<SavePushTokenResponse> savePushToken(@RequestHeader("x-user-id") final String userId,
                                                               @RequestBody final PushTokenRequest request) {
        return ResponseEntity.ok(this.userPushTokenService.savePushToken(userId, request));
    }

}

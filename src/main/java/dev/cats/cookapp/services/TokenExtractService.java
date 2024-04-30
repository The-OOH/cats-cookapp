package dev.cats.cookapp.services;

import dev.cats.cookapp.models.User;
import dev.cats.cookapp.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenExtractService {
    private final UserService userService;
    public Optional<User> extractToken(Authentication authentication){
        String email = authentication.getName();

        return userService.getUserModel(email);
    }
}

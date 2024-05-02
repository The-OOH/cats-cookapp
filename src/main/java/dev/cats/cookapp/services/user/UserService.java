package dev.cats.cookapp.services.user;

import dev.cats.cookapp.dto.request.UserRequest;
import dev.cats.cookapp.dto.response.UserResponse;
import dev.cats.cookapp.models.User;

import java.util.Optional;

public interface UserService {
    Optional<UserResponse> getUser(String email);
    Optional<User> getCurrentUser();
    Optional<User> getUserModel(String email);
    UserResponse createUser(UserRequest userRequest);
}

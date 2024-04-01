package dev.cats.cookapp.services.user;

import dev.cats.cookapp.dto.request.UserRequest;
import dev.cats.cookapp.dto.response.UserResponse;

import java.util.Optional;

public interface UserService {
    Optional<UserResponse> getUser(Long id);
    Optional<UserResponse> getUser(String email);
    UserResponse createUser(UserRequest userRequest);
    UserResponse updateUser(UserRequest userRequest);
    void deleteUser(Long id);
}

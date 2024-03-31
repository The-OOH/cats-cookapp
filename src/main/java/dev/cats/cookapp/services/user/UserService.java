package dev.cats.cookapp.services.user;

import dev.cats.cookapp.dto.response.UserResponse;

import java.util.Optional;

public interface UserService {
    Optional<UserResponse> getUser(Long id);
    Optional<UserResponse> getUser(String email);
//    UserResponse createUser(String email, String username);
//    UserResponse updateUser(Long id, String email, String username);
    void deleteUser(Long id);
}

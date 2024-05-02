package dev.cats.cookapp.services.user;

import dev.cats.cookapp.dto.request.UserRequest;
import dev.cats.cookapp.dto.response.UserResponse;
import dev.cats.cookapp.mappers.UserMapper;
import dev.cats.cookapp.models.User;
import dev.cats.cookapp.models.UserList;
import dev.cats.cookapp.repositories.UserListRepository;
import dev.cats.cookapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserListRepository userListRepository;

    @Override
    public Optional<UserResponse> getUser(String email) {
        return userRepository.findByEmail(email).map(userMapper::toDto);
    }

    @Override
    public Optional<User> getUserModel(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserResponse createUser(UserRequest userRequest) {
        userRequest.setId(null);
        userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        var user = userRepository.save(userMapper.toEntity(userRequest));

        var list  = userListRepository.save(new UserList("Faves", user));
        user.getUserLists().add(list);
        userRepository.save(user);

        return userMapper.toDto(user);
    }

    @Override
    public Optional<User> getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return Optional.empty();
        }
        return getUserModel(auth.getName());
    }
}

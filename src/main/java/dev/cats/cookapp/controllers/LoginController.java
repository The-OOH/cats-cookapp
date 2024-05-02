package dev.cats.cookapp.controllers;

import dev.cats.cookapp.dto.request.UserRequest;
import dev.cats.cookapp.dto.response.UserResponse;
import dev.cats.cookapp.dto.response.UserTokenDto;
import dev.cats.cookapp.models.User;
import dev.cats.cookapp.services.JwtUtil;
import dev.cats.cookapp.services.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/sign-in")
    public ResponseEntity<UserTokenDto> login(@RequestBody @Valid UserRequest userDto) {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                        (userDto.getEmail(), userDto.getPassword()));
        String login = authentication.getName();

        User user = userService.getUserModel(userDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String token = jwtUtil.createToken(user, "USER");
        UserTokenDto loginRes = new UserTokenDto(login, token);

        return ResponseEntity.ok(loginRes);
    }

    @PutMapping("/sign-up")
    public ResponseEntity<UserTokenDto> register(@RequestBody UserRequest userDto) {
        userService.createUser(userDto);

        var token = jwtUtil.createToken(userService.getUserModel(userDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found")), "USER");

        return ResponseEntity.ok(new UserTokenDto(userDto.getEmail(), token));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCredential(){
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var user = userService.getUser(auth.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return ResponseEntity.ok(user);
    }
}

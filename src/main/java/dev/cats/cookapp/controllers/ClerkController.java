package dev.cats.cookapp.controllers;


import dev.cats.cookapp.dtos.UserDetails;
import dev.cats.cookapp.services.ClerkService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/clerk")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ClerkController {
    ClerkService clerkService;

    @GetMapping("/users")
    public ResponseEntity<List<UserDetails>> getAllUsers() {
        return ResponseEntity.ok(clerkService.listAllUsers());
    }

}

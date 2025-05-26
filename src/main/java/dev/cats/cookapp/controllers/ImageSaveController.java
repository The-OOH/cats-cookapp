package dev.cats.cookapp.controllers;

import dev.cats.cookapp.dtos.response.ImageSaveResponse;
import dev.cats.cookapp.services.CloudflareR2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageSaveController {
    private final CloudflareR2Service service;

    @GetMapping("/pre-signed-url")
    public ResponseEntity<ImageSaveResponse> getPreSignedUrl(@RequestParam("key") String key,
                                                             @RequestParam(name = "duration", defaultValue = "5") Integer duration) {
        return ResponseEntity.ok(service.getPresignedUrl(key, Duration.ofMinutes(duration)));
    }
}

package dev.cats.cookapp.dto.response;

import lombok.Value;

@Value
public class UserTokenDto {
    String email;
    String token;
}

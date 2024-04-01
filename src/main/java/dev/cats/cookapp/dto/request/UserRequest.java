package dev.cats.cookapp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for {@link dev.cats.cookapp.models.User}
 */
@Getter
@Setter
@AllArgsConstructor
public class UserRequest {
    Long id;
    String email;
    String username;
}

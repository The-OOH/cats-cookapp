package dev.cats.cookapp.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "Email is mandatory") @Email(message = "Email should be valid")
    String email;
    String username;
    @NotBlank(message = "Password is mandatory")
    String password;
}

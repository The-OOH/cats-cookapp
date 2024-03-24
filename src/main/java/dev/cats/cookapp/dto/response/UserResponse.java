package dev.cats.cookapp.dto.response;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link dev.cats.cookapp.models.User}
 */
@Value
public class UserResponse implements Serializable {
    Long id;
}
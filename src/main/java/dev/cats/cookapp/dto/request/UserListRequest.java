package dev.cats.cookapp.dto.request;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link dev.cats.cookapp.models.UserList}
 */
public record UserListRequest(Long id, String name, Set<Long> recipeIds) implements Serializable {
}
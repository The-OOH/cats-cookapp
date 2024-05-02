package dev.cats.cookapp.dto.response;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link dev.cats.cookapp.models.UserList}
 */
public record UserListResponse(Long id, String name, Set<RecipeListResponse> recipes) implements Serializable {
}
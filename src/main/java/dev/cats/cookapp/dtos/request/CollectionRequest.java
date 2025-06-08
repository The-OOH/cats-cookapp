package dev.cats.cookapp.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CollectionRequest {
    Long id;
    @NotNull
    String name;
    String description;
    List<Long> recipeIds;
}

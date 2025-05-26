package dev.cats.cookapp.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CollectionRequest {
    Long id;
    @NotNull
    String name;
    String description;
    @NotNull
    List<Long> recipeIds;
}

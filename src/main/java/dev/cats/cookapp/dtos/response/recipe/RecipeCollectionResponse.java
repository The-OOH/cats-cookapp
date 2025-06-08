package dev.cats.cookapp.dtos.response.recipe;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RecipeCollectionResponse {
    Long id;

    String mainImageUrl;
}

package dev.cats.cookapp.dto.request;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Value
@FieldDefaults(makeFinal = true)
@RequiredArgsConstructor
public class RecipeRequest {
    String title;
    Integer price;
    Integer time;
    Integer servings;
    String image;
    List<String> categories;
    List<String> steps;
    List<RecipeProductRequest> products;

}

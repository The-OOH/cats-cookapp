package dev.cats.cookapp.dto.request;


import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.FieldDefaults;

@Value
@FieldDefaults(makeFinal = true)
@RequiredArgsConstructor
public class RecipeProductRequest {
    Long productId;
    String customName;
    Double amount;
    Long unitId;
}

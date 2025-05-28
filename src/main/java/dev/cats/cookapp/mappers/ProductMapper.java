package dev.cats.cookapp.mappers;

import dev.cats.cookapp.dtos.response.ProductSuggestionResponse;
import dev.cats.cookapp.models.recipe.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductSuggestionResponse toSuggestion(Product product);
}


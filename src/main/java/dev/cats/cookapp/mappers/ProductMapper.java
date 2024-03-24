package dev.cats.cookapp.mappers;

import dev.cats.cookapp.dto.response.ProductResponse;
import dev.cats.cookapp.models.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductResponse toDto(Product product);
}

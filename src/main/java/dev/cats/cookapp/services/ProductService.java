package dev.cats.cookapp.services;

import dev.cats.cookapp.dtos.response.ProductSuggestionResponse;

import java.util.List;

public interface ProductService {
    List<ProductSuggestionResponse> getProductSuggestions(String name);
}

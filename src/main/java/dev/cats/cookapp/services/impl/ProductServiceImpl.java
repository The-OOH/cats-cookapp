package dev.cats.cookapp.services.impl;

import dev.cats.cookapp.dtos.response.ProductSuggestionResponse;
import dev.cats.cookapp.mappers.ProductMapper;
import dev.cats.cookapp.repositories.ProductRepository;
import dev.cats.cookapp.services.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public List<ProductSuggestionResponse> getProductSuggestions(String name) {
        return productRepository.findTop10ByNameIgnoreCaseContaining(name)
                .stream()
                .map(productMapper::toSuggestion)
                .toList();
    }
}

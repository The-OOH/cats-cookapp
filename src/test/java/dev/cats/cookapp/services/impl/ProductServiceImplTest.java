package dev.cats.cookapp.services.impl;

import dev.cats.cookapp.dtos.response.ProductSuggestionResponse;
import dev.cats.cookapp.mappers.ProductMapper;
import dev.cats.cookapp.models.recipe.Product;
import dev.cats.cookapp.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    @DisplayName("getProductSuggestions should return mapped suggestions when products are found")
    void givenExistingProducts_whenGetProductSuggestions_thenReturnMappedResponses() {
        // Arrange
        final String query = "apple";
        final Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Green Apple");
        final Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Red Apple");

        final List<Product> products = List.of(product1, product2);
        when(this.productRepository.findTop10ByNameIgnoreCaseContaining(query)).thenReturn(products);

        final ProductSuggestionResponse response1 = new ProductSuggestionResponse(1L, "Green Apple");
        final ProductSuggestionResponse response2 = new ProductSuggestionResponse(2L, "Red Apple");
        when(this.productMapper.toSuggestion(product1)).thenReturn(response1);
        when(this.productMapper.toSuggestion(product2)).thenReturn(response2);

        // Act
        final List<ProductSuggestionResponse> result = this.productService.getProductSuggestions(query);

        // Assert
        verify(this.productRepository).findTop10ByNameIgnoreCaseContaining(query);
        verify(this.productMapper).toSuggestion(product1);
        verify(this.productMapper).toSuggestion(product2);
        assertThat(result).containsExactly(response1, response2);
    }

    @Test
    @DisplayName("getProductSuggestions should return empty list when no products are found")
    void givenNoProducts_whenGetProductSuggestions_thenReturnEmptyList() {
        // Arrange
        final String query = "banana";
        when(this.productRepository.findTop10ByNameIgnoreCaseContaining(query)).thenReturn(Collections.emptyList());

        // Act
        final List<ProductSuggestionResponse> result = this.productService.getProductSuggestions(query);

        // Assert
        verify(this.productRepository).findTop10ByNameIgnoreCaseContaining(query);
        assertThat(result).isEmpty();
        verifyNoInteractions(this.productMapper);
    }
}

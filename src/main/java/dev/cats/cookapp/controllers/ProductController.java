package dev.cats.cookapp.controllers;

import dev.cats.cookapp.dtos.response.ProductSuggestionResponse;
import dev.cats.cookapp.services.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ProductController {

    ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductSuggestionResponse>> getIngredientsByName(
            @RequestParam(name = "name") String name) {
        return ResponseEntity.ok(this.productService.getProductSuggestions(name));
    }
}

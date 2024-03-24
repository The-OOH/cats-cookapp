package dev.cats.cookapp.controllers;

import dev.cats.cookapp.dto.response.ProductResponse;
import dev.cats.cookapp.services.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public List<ProductResponse> getProducts(){
        return productService.getProducts();
    }
}

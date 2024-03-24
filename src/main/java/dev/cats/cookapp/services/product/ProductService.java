package dev.cats.cookapp.services.product;

import dev.cats.cookapp.dto.response.ProductResponse;

import java.util.List;

public interface ProductService {
    List<ProductResponse> getProducts();
}

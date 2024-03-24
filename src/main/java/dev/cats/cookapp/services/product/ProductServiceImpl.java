package dev.cats.cookapp.services.product;

import dev.cats.cookapp.dto.response.ProductResponse;
import dev.cats.cookapp.mappers.ProductMapper;
import dev.cats.cookapp.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public List<ProductResponse> getProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }
}

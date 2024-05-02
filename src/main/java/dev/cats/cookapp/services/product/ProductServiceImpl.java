package dev.cats.cookapp.services.product;

import dev.cats.cookapp.dto.response.ProductResponse;
import dev.cats.cookapp.mappers.ProductMapper;
import dev.cats.cookapp.models.Product;
import dev.cats.cookapp.repositories.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;
    private final EntityManager entityManager;
    private final ProductMapper productMapper;

    public List<ProductResponse> getProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> searchProducts(String searchTerm) {
        SearchSession searchSession = Search.session(entityManager);
        try {
            searchSession.massIndexer().startAndWait();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        List<Product> products = searchSession.search(Product.class)
                .where(f -> f.match().fields("name").matching(searchTerm).fuzzy())
                .fetchHits(20);  // fetch a reasonable number of hits

        return products.stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }
}

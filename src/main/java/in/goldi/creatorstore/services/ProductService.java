package in.goldi.creatorstore.services;

import in.goldi.creatorstore.dto.ProductResponse;
import in.goldi.creatorstore.entities.Product;
import in.goldi.creatorstore.exceptions.ResourceNotFoundException;
import in.goldi.creatorstore.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;


    // =========================
    // CREATE PRODUCT
    // =========================

    public ProductResponse createProduct(Product product) {

        Product savedProduct = productRepository.save(product);

        return toResponse(savedProduct);
    }


    // =========================
    // GET ALL PRODUCTS
    // =========================

    public List<ProductResponse> getProducts() {

        return productRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }


    // =========================
    // GET PRODUCTS WITH PAGINATION
    // =========================

    public Page<ProductResponse> getProducts(Pageable pageable) {

        return productRepository.findAll(pageable)
                .map(this::toResponse);
    }


    // =========================
    // GET PRODUCT BY ID
    // =========================

    public ProductResponse getProductById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id " + id
                        )
                );

        return toResponse(product);
    }


    // =========================
    // UPDATE PRODUCT
    // =========================

    public ProductResponse updateProduct(
            Long id,
            Product product
    ) {

        Product existingProduct =
                productRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Product not found with id " + id
                                )
                        );

        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setStockQuantity(
                product.getStockQuantity()
        );

        Product updatedProduct =
                productRepository.save(existingProduct);

        return toResponse(updatedProduct);
    }


    // =========================
    // DELETE PRODUCT
    // =========================

    public void deleteProductById(Long id) {

        Product product =
                productRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Product not found with id " + id
                                )
                        );

        productRepository.delete(product);
    }


    // =========================
    // SEARCH PRODUCT
    // =========================

    public List<ProductResponse> searchProducts(
            String name
    ) {

        return productRepository
                .findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::toResponse)
                .toList();
    }


    // =========================
    // PRODUCTS BY CATEGORY
    // =========================

    public List<ProductResponse> getProductsByCategory(
            String category
    ) {

        return productRepository
                .findByCategoryIgnoreCase(category)
                .stream()
                .map(this::toResponse)
                .toList();
    }


    // =========================
    // LOW STOCK PRODUCTS
    // =========================

    public List<ProductResponse> getLowStockProducts(
            Integer quantity
    ) {

        return productRepository
                .findByStockQuantityLessThan(quantity)
                .stream()
                .map(this::toResponse)
                .toList();
    }


    // =========================
    // ENTITY → DTO
    // =========================

    public ProductResponse toResponse(Product product) {

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .category(product.getCategory())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .build();
    }
}
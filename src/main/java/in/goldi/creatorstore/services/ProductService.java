package in.goldi.creatorstore.services;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import in.goldi.creatorstore.entities.Product;
import in.goldi.creatorstore.exceptions.ResourceNotFoundException;
import in.goldi.creatorstore.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // CREATE PRODUCT
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    // GET ALL PRODUCTS
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    // GET PRODUCT BY ID
    public Product getProductById(Long id) {

        return productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with id " + id
                        )
                );
    }

    // UPDATE PRODUCT
    public Product updateProduct(Long id, Product product) {

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
        existingProduct.setStockQuantity(product.getStockQuantity());

        return productRepository.save(existingProduct);
    }

    // DELETE PRODUCT
    public void deleteProductById(Long id) {

        Product product = getProductById(id);

        productRepository.delete(product);
    }

    // SEARCH PRODUCT
    public List<Product> searchProducts(String name) {

        return productRepository
                .findByNameContainingIgnoreCase(name);
    }

    // GET PRODUCTS BY CATEGORY
    public List<Product> getProductsByCategory(String category) {

        return productRepository
                .findByCategoryIgnoreCase(category);
    }
    public Page<Product> getProducts(
            Pageable pageable
    ) {

        return productRepository.findAll(pageable);
    }
}
package in.goldi.creatorstore.controllers;

import in.goldi.creatorstore.entities.Product;
import in.goldi.creatorstore.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // CREATE PRODUCT
    @PostMapping
    public Product createProduct(
            @Valid @RequestBody Product product
    ) {
        return productService.createProduct(product);
    }

    // GET ALL PRODUCTS
    @GetMapping
    public List<Product> getProducts() {
        return productService.getProducts();
    }

    // GET PRODUCT BY ID
    @GetMapping("/{id}")
    public Product getProductById(
            @PathVariable Long id
    ) {
        return productService.getProductById(id);
    }

    // UPDATE PRODUCT
    @PutMapping("/{id}")
    public Product updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody Product product
    ) {
        return productService.updateProduct(id, product);
    }

    // DELETE PRODUCT
    @DeleteMapping("/{id}")
    public void deleteProductById(
            @PathVariable Long id
    ) {
        productService.deleteProductById(id);
    }

    // SEARCH PRODUCT
    @GetMapping("/search")
    public List<Product> searchProducts(
            @RequestParam String name
    ) {
        return productService.searchProducts(name);
    }

    // PRODUCTS BY CATEGORY
    @GetMapping("/category/{category}")
    public List<Product> getProductsByCategory(
            @PathVariable String category
    ) {
        return productService.getProductsByCategory(category);
    }
}
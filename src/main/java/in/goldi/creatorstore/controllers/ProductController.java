package in.goldi.creatorstore.controllers;

import in.goldi.creatorstore.dto.ProductResponse;
import in.goldi.creatorstore.entities.Product;
import in.goldi.creatorstore.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;


    // =========================
    // CREATE PRODUCT
    // =========================

    @PostMapping
    public ProductResponse createProduct(
            @Valid @RequestBody Product product
    ) {

        return productService.createProduct(product);
    }


    // =========================
    // GET ALL PRODUCTS
    // =========================

    @GetMapping
    public List<ProductResponse> getProducts() {

        return productService.getProducts();
    }


    // =========================
    // GET PRODUCTS WITH PAGINATION
    // =========================

    @GetMapping("/page")
    public Page<ProductResponse> getProductsWithPagination(
            Pageable pageable
    ) {

        return productService.getProducts(pageable);
    }


    // =========================
    // GET PRODUCT BY ID
    // =========================

    @GetMapping("/{id}")
    public ProductResponse getProductById(
            @PathVariable Long id
    ) {

        return productService.getProductById(id);
    }


    // =========================
    // UPDATE PRODUCT
    // =========================

    @PutMapping("/{id}")
    public ProductResponse updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody Product product
    ) {

        return productService.updateProduct(
                id,
                product
        );
    }


    // =========================
    // DELETE PRODUCT
    // =========================

    @DeleteMapping("/{id}")
    public void deleteProductById(
            @PathVariable Long id
    ) {

        productService.deleteProductById(id);
    }


    // =========================
    // SEARCH PRODUCT
    // =========================

    @GetMapping("/search")
    public List<ProductResponse> searchProducts(
            @RequestParam String name
    ) {

        return productService.searchProducts(name);
    }


    // =========================
    // PRODUCTS BY CATEGORY
    // =========================

    @GetMapping("/category/{category}")
    public List<ProductResponse> getProductsByCategory(
            @PathVariable String category
    ) {

        return productService.getProductsByCategory(
                category
        );
    }


    // =========================
    // LOW STOCK PRODUCTS
    // =========================

    @GetMapping("/low-stock")
    public List<ProductResponse> getLowStockProducts(
            @RequestParam(defaultValue = "10") Integer quantity
    ) {

        return productService.getLowStockProducts(quantity);
    }
}
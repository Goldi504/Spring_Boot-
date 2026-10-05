package in.goldi.creatorstore.repositories;

import in.goldi.creatorstore.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository
        extends JpaRepository<Product, Long> {

    List<Product> findByCategoryIgnoreCase(
            String category
    );

    List<Product> findByNameContainingIgnoreCase(
            String name
    );

    List<Product> findByStockQuantityLessThan(
            Integer quantity
    );
}
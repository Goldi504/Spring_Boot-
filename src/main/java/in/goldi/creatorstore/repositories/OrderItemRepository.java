package in.goldi.creatorstore.repositories;

import in.goldi.creatorstore.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem ,Long> {
}

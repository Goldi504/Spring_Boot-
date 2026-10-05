package in.goldi.creatorstore.repositories;
import in.goldi.creatorstore.entities.OrderStatus;

import in.goldi.creatorstore.entities.Order;
import in.goldi.creatorstore.entities.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository
        extends JpaRepository<Order, Long> {

    List<Order> findAllByOrderByCreatedAtDesc();

    List<Order>
    findByCustomerEmailIgnoreCaseOrderByCreatedAtDesc(
            String customerEmail
    );

    List<Order>
    findByUser_IdOrderByCreatedAtDesc(
            Long userId
    );

    @Query("""
        SELECT COALESCE(SUM(o.totalPrice), 0)
        FROM Order o
        WHERE o.status <> :cancelledStatus
        """)
    BigDecimal getTotalSales(
            OrderStatus cancelledStatus
    );

    @Query("""
        SELECT COALESCE(SUM(o.totalPrice), 0)
        FROM Order o
        WHERE o.createdAt >= :start
        AND o.createdAt < :end
        AND o.status <> :cancelledStatus
        """)
    BigDecimal getSalesBetween(
            LocalDateTime start,
            LocalDateTime end,
            OrderStatus cancelledStatus
    );
}
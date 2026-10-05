package in.goldi.creatorstore.services;

import in.goldi.creatorstore.dto.DashboardResponse;
import in.goldi.creatorstore.entities.OrderStatus;
import in.goldi.creatorstore.entities.Role;
import in.goldi.creatorstore.repositories.OrderRepository;
import in.goldi.creatorstore.repositories.ProductRepository;
import in.goldi.creatorstore.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    private final OrderRepository orderRepository;

    public DashboardResponse getDashboard() {

        long totalCustomers =
                userRepository.countByRole(
                        Role.CUSTOMER
                );

        long totalProducts =
                productRepository.count();

        long totalOrders =
                orderRepository.count();

        BigDecimal totalSales =
                orderRepository.getTotalSales(
                   OrderStatus.CANCELLED
                   );

        LocalDateTime startOfDay =
                LocalDate.now().atStartOfDay();

        LocalDateTime endOfDay =
                LocalDate.now()
                        .plusDays(1)
                        .atStartOfDay();

        BigDecimal todaySales =
                orderRepository.getSalesBetween(
                        startOfDay,
                        endOfDay,
                        OrderStatus.CANCELLED
                );

        long lowStockProducts =
                productRepository
                        .findByStockQuantityLessThan(10)
                        .size();

        return DashboardResponse.builder()
                .totalCustomers(totalCustomers)
                .totalProducts(totalProducts)
                .totalOrders(totalOrders)
                .totalSales(totalSales)
                .todaySales(todaySales)
                .lowStockProducts(lowStockProducts)
                .build();
    }
}
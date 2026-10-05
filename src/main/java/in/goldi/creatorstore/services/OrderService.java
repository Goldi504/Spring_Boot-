package in.goldi.creatorstore.services;

import in.goldi.creatorstore.dto.OrderItemRequest;
import in.goldi.creatorstore.dto.OrderRequest;
import in.goldi.creatorstore.entities.Order;
import in.goldi.creatorstore.entities.OrderItem;
import in.goldi.creatorstore.entities.Product;
import in.goldi.creatorstore.repositories.OrderRepository;
import in.goldi.creatorstore.repositories.ProductRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Order createOrder(OrderRequest orderRequest) {

        List<OrderItem> orderItems = new ArrayList<>();

        BigDecimal totalPrice = BigDecimal.ZERO;

        Order order = new Order();

        order.setCustomerName(orderRequest.getCustomerName());
        order.setCustomerEmail(orderRequest.getCustomerEmail());
        order.setStatus("CONFIRMED");

        for (OrderItemRequest itemRequest : orderRequest.getItems()) {

            Product product = productRepository.findById(
                    Long.valueOf(itemRequest.getProductId())
            ).orElseThrow(() ->
                    new RuntimeException(
                            "Product not found with id "
                                    + itemRequest.getProductId()
                    )
            );

            // Check stock
            if (product.getStockQuantity() < itemRequest.getQuantity()) {

                throw new RuntimeException(
                        "Not enough stock for product "
                                + product.getName()
                );
            }

            // Calculate item price
            BigDecimal priceOfItem = product.getPrice()
                    .multiply(
                            BigDecimal.valueOf(
                                    itemRequest.getQuantity()
                            )
                    );

            // Add to total
            totalPrice = totalPrice.add(priceOfItem);

            // Reduce stock
            product.setStockQuantity(
                    product.getStockQuantity()
                            - itemRequest.getQuantity()
            );

            // Save updated product
            productRepository.save(product);

            // Create order item
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(itemRequest.getQuantity())
                    .priceAtPurchase(product.getPrice())
                    .build();

            orderItems.add(orderItem);
        }

        // Set order details
        order.setTotalPrice(totalPrice);
        order.setOrderItems(orderItems);

        // Save order
        return orderRepository.save(order);
    }
}
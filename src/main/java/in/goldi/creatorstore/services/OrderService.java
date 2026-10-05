package in.goldi.creatorstore.services;

import in.goldi.creatorstore.dto.OrderItemRequest;
import in.goldi.creatorstore.dto.OrderRequest;
import in.goldi.creatorstore.entities.*;
import in.goldi.creatorstore.exceptions.InsufficientStockException;
import in.goldi.creatorstore.exceptions.ResourceNotFoundException;
import in.goldi.creatorstore.repositories.OrderRepository;
import in.goldi.creatorstore.repositories.ProductRepository;
import in.goldi.creatorstore.repositories.UserRepository;
import in.goldi.creatorstore.entities.OrderStatus;

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
    private final UserRepository userRepository;

    // ==========================================
    // CREATE ORDER
    // ==========================================

    @Transactional
    public Order createOrder(
            OrderRequest request,
            String customerEmail
    ) {

        User user = userRepository
                .findByEmailIgnoreCase(customerEmail)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        Order order = new Order();

        order.setUser(user);
        order.setCustomerName(user.getName());
        order.setCustomerEmail(user.getEmail());
        order.setStatus(OrderStatus.CONFIRMED);

        List<OrderItem> orderItems = new ArrayList<>();

        BigDecimal totalPrice = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : request.getItems()) {

            Product product = productRepository
                    .findById(itemRequest.getProductId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Product not found with id "
                                            + itemRequest.getProductId()
                            )
                    );

            if (product.getStockQuantity()
                    < itemRequest.getQuantity()) {

                throw new InsufficientStockException(
                        "Not enough stock for product: "
                                + product.getName()
                );
            }

            BigDecimal itemTotal =
                    product.getPrice()
                            .multiply(
                                    BigDecimal.valueOf(
                                            itemRequest.getQuantity()
                                    )
                            );

            totalPrice = totalPrice.add(itemTotal);

            product.setStockQuantity(
                    product.getStockQuantity()
                            - itemRequest.getQuantity()
            );

            productRepository.save(product);

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(itemRequest.getQuantity())
                    .priceAtPurchase(product.getPrice())
                    .build();

            orderItems.add(orderItem);
        }

        order.setTotalPrice(totalPrice);
        order.setOrderItems(orderItems);

        return orderRepository.save(order);
    }

    // ==========================================
    // CUSTOMER ORDERS
    // ==========================================

    public List<Order> getMyOrders(String email) {

        return orderRepository
                .findByCustomerEmailIgnoreCaseOrderByCreatedAtDesc(
                        email
                );
    }

    // ==========================================
    // GET CUSTOMER ORDER BY ID
    // ==========================================

    public Order getMyOrderById(
            Long orderId,
            String email
    ) {

        Order order = getOrderById(orderId);

        if (!order.getCustomerEmail()
                .equalsIgnoreCase(email)) {

            throw new ResourceNotFoundException(
                    "Order not found"
            );
        }

        return order;
    }

    // ==========================================
    // ADMIN - GET ALL ORDERS
    // ==========================================

    public List<Order> getAllOrders() {

        return orderRepository
                .findAllByOrderByCreatedAtDesc();
    }

    // ==========================================
    // GET ORDER BY ID
    // ==========================================

    public Order getOrderById(Long id) {

        return orderRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found with id " + id
                        )
                );
    }

    // ==========================================
    // ADMIN - UPDATE ORDER STATUS
    // ==========================================

    @Transactional
    public Order updateOrderStatus(
            Long id,
            String status
    ) {

        Order order = getOrderById(id);

        try {

            OrderStatus newStatus =
                    OrderStatus.valueOf(
                            status.toUpperCase()
                    );

            order.setStatus(newStatus);

        } catch (IllegalArgumentException ex) {

            throw new IllegalArgumentException(
                    "Invalid order status: " + status
            );
        }

        return orderRepository.save(order);
    }

    // ==========================================
    // CUSTOMER - CANCEL OWN ORDER
    // ==========================================

    @Transactional
    public void cancelMyOrder(
            Long id,
            String email
    ) {

        Order order = getMyOrderById(id, email);

        if (order.getStatus() == OrderStatus.CANCELLED) {
            return;
        }

        if ("DELIVERED".equals(order.getStatus())) {
            throw new IllegalStateException(
                    "Delivered order cannot be cancelled"
            );
        }

        for (OrderItem item : order.getOrderItems()) {

            Product product = item.getProduct();

            product.setStockQuantity(
                    product.getStockQuantity()
                            + item.getQuantity()
            );

            productRepository.save(product);
        }

        order.setStatus(OrderStatus.CANCELLED);

        orderRepository.save(order);
    }
}
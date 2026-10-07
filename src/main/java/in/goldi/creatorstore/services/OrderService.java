package in.goldi.creatorstore.services;

import in.goldi.creatorstore.dto.OrderItemRequest;
import in.goldi.creatorstore.dto.OrderItemResponse;
import in.goldi.creatorstore.dto.OrderRequest;
import in.goldi.creatorstore.dto.OrderResponse;
import in.goldi.creatorstore.entities.Order;
import in.goldi.creatorstore.entities.OrderItem;
import in.goldi.creatorstore.entities.OrderStatus;
import in.goldi.creatorstore.entities.Product;
import in.goldi.creatorstore.entities.User;
import in.goldi.creatorstore.exceptions.InsufficientStockException;
import in.goldi.creatorstore.exceptions.ResourceNotFoundException;
import in.goldi.creatorstore.repositories.OrderRepository;
import in.goldi.creatorstore.repositories.ProductRepository;
import in.goldi.creatorstore.repositories.UserRepository;
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


            // CHECK STOCK

            if (product.getStockQuantity()
                    < itemRequest.getQuantity()) {

                throw new InsufficientStockException(
                        "Not enough stock for product: "
                                + product.getName()
                );
            }


            // CALCULATE ITEM TOTAL

            BigDecimal itemTotal =
                    product.getPrice()
                            .multiply(
                                    BigDecimal.valueOf(
                                            itemRequest.getQuantity()
                                    )
                            );

            totalPrice =
                    totalPrice.add(itemTotal);


            // REDUCE STOCK

            product.setStockQuantity(
                    product.getStockQuantity()
                            - itemRequest.getQuantity()
            );

            productRepository.save(product);


            // CREATE ORDER ITEM

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

    public List<OrderResponse> getMyOrders(
            String email
    ) {

        return orderRepository
                .findByCustomerEmailIgnoreCaseOrderByCreatedAtDesc(
                        email
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }


    // ==========================================
    // CUSTOMER ORDER BY ID
    // ==========================================

    public OrderResponse getMyOrderById(
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

        return toResponse(order);
    }


    // ==========================================
    // ADMIN - ALL ORDERS
    // ==========================================

    public List<OrderResponse> getAllOrders() {

        return orderRepository
                .findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }


    // ==========================================
    // INTERNAL GET ORDER
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

            OrderStatus orderStatus =
                    OrderStatus.valueOf(
                            status.trim().toUpperCase()
                    );

            order.setStatus(orderStatus);

            return orderRepository.save(order);

        } catch (IllegalArgumentException ex) {

            throw new IllegalArgumentException(
                    "Invalid order status. Allowed values: "
                            + "PENDING, CONFIRMED, PROCESSING, "
                            + "SHIPPED, DELIVERED, CANCELLED"
            );
        }
    }


    // ==========================================
    // CUSTOMER - CANCEL ORDER
    // ==========================================

    @Transactional
    public void cancelMyOrder(
            Long id,
            String email
    ) {

        Order order = getOrderById(id);


        // SECURITY CHECK

        if (!order.getCustomerEmail()
                .equalsIgnoreCase(email)) {

            throw new ResourceNotFoundException(
                    "Order not found"
            );
        }


        // ALREADY CANCELLED

        if (order.getStatus() == OrderStatus.CANCELLED) {
            return;
        }


        // DELIVERED ORDER CANNOT BE CANCELLED

        if (order.getStatus() == OrderStatus.DELIVERED) {

            throw new IllegalStateException(
                    "Delivered order cannot be cancelled"
            );
        }


        // RESTORE STOCK

        for (OrderItem item : order.getOrderItems()) {

            Product product = item.getProduct();

            product.setStockQuantity(
                    product.getStockQuantity()
                            + item.getQuantity()
            );

            productRepository.save(product);
        }


        // UPDATE STATUS

        order.setStatus(OrderStatus.CANCELLED);

        orderRepository.save(order);
    }


    // ==========================================
    // ENTITY → RESPONSE DTO
    // ==========================================

    public OrderResponse toResponse(Order order) {

        List<OrderItemResponse> items =
                order.getOrderItems()
                        .stream()
                        .map(item -> {

                            BigDecimal itemTotal =
                                    item.getPriceAtPurchase()
                                            .multiply(
                                                    BigDecimal.valueOf(
                                                            item.getQuantity()
                                                    )
                                            );

                            return OrderItemResponse.builder()
                                    .id(item.getId())
                                    .productId(
                                            item.getProduct().getId()
                                    )
                                    .productName(
                                            item.getProduct().getName()
                                    )
                                    .quantity(
                                            item.getQuantity()
                                    )
                                    .priceAtPurchase(
                                            item.getPriceAtPurchase()
                                    )
                                    .itemTotal(itemTotal)
                                    .build();
                        })
                        .toList();


        return OrderResponse.builder()
                .id(order.getId())
                .customerName(order.getCustomerName())
                .customerEmail(order.getCustomerEmail())
                .status(order.getStatus())
                .totalPrice(order.getTotalPrice())
                .createdAt(order.getCreatedAt())
                .items(items)
                .build();
    }
}
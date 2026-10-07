package in.goldi.creatorstore.controllers;

import in.goldi.creatorstore.dto.OrderRequest;
import in.goldi.creatorstore.dto.OrderResponse;
import in.goldi.creatorstore.entities.Order;
import in.goldi.creatorstore.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;


    // ==========================================
    // CREATE ORDER
    // ==========================================

    @PostMapping
    public OrderResponse createOrder(
            @Valid @RequestBody OrderRequest request,
            Authentication authentication
    ) {

        Order order =
                orderService.createOrder(
                        request,
                        authentication.getName()
                );

        return orderService.toResponse(order);
    }


    // ==========================================
    // MY ORDERS
    // ==========================================

    @GetMapping("/my-orders")
    public List<OrderResponse> getMyOrders(
            Authentication authentication
    ) {

        return orderService.getMyOrders(
                authentication.getName()
        );
    }


    // ==========================================
    // MY ORDER BY ID
    // ==========================================

    @GetMapping("/{id}")
    public OrderResponse getMyOrderById(
            @PathVariable Long id,
            Authentication authentication
    ) {

        return orderService.getMyOrderById(
                id,
                authentication.getName()
        );
    }


    // ==========================================
    // CANCEL MY ORDER
    // ==========================================

    @PutMapping("/{id}/cancel")
    public String cancelOrder(
            @PathVariable Long id,
            Authentication authentication
    ) {

        orderService.cancelMyOrder(
                id,
                authentication.getName()
        );

        return "Order cancelled successfully";
    }
}
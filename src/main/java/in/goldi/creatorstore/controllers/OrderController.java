package in.goldi.creatorstore.controllers;

import in.goldi.creatorstore.dto.OrderRequest;
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
    // CUSTOMER - CREATE ORDER
    // ==========================================

    @PostMapping
    public Order createOrder(
            @Valid @RequestBody OrderRequest request,
            Authentication authentication
    ) {

        return orderService.createOrder(
                request,
                authentication.getName()
        );
    }

    // ==========================================
    // CUSTOMER - MY ORDERS
    // ==========================================

    @GetMapping("/my-orders")
    public List<Order> getMyOrders(
            Authentication authentication
    ) {

        return orderService.getMyOrders(
                authentication.getName()
        );
    }

    // ==========================================
    // CUSTOMER - MY ORDER
    // ==========================================

    @GetMapping("/{id}")
    public Order getMyOrderById(
            @PathVariable Long id,
            Authentication authentication
    ) {

        return orderService.getMyOrderById(
                id,
                authentication.getName()
        );
    }

    // ==========================================
    // CUSTOMER - CANCEL ORDER
    // ==========================================

    @PutMapping("/{id}/cancel")
    public void cancelOrder(
            @PathVariable Long id,
            Authentication authentication
    ) {

        orderService.cancelMyOrder(
                id,
                authentication.getName()
        );
    }
}
package in.goldi.creatorstore.controllers;

import in.goldi.creatorstore.dto.OrderResponse;
import in.goldi.creatorstore.entities.Order;
import in.goldi.creatorstore.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;


    // ==========================================
    // GET ALL ORDERS
    // ==========================================

    @GetMapping
    public List<OrderResponse> getAllOrders() {

        return orderService.getAllOrders();
    }


    // ==========================================
    // GET ORDER BY ID
    // ==========================================

    @GetMapping("/{id}")
    public OrderResponse getOrderById(
            @PathVariable Long id
    ) {

        Order order =
                orderService.getOrderById(id);

        return orderService.toResponse(order);
    }


    // ==========================================
    // UPDATE ORDER STATUS
    // ==========================================

    @PutMapping("/{id}/status")
    public OrderResponse updateOrderStatus(
            @PathVariable Long id,
            @RequestParam String status
    ) {

        Order order =
                orderService.updateOrderStatus(
                        id,
                        status
                );

        return orderService.toResponse(order);
    }
}
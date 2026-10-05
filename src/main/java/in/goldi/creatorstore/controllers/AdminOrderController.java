package in.goldi.creatorstore.controllers;

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
    // ADMIN - ALL ORDERS
    // ==========================================

    @GetMapping
    public List<Order> getAllOrders() {

        return orderService.getAllOrders();
    }

    // ==========================================
    // ADMIN - GET ORDER
    // ==========================================

    @GetMapping("/{id}")
    public Order getOrderById(
            @PathVariable Long id
    ) {

        return orderService.getOrderById(id);
    }

    // ==========================================
    // ADMIN - UPDATE STATUS
    // ==========================================

    @PutMapping("/{id}/status")
    public Order updateOrderStatus(
            @PathVariable Long id,
            @RequestParam String status
    ) {

        return orderService.updateOrderStatus(
                id,
                status
        );
    }
}
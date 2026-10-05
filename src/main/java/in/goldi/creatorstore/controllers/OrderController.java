package in.goldi.creatorstore.controllers;

import in.goldi.creatorstore.dto.OrderRequest;
import in.goldi.creatorstore.entities.Order;
import in.goldi.creatorstore.serivces.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    @PostMapping
    public Order createOrder(@Valid @RequestBody OrderRequest orderRequest){
        return orderService.createOrder(orderRequest);

    }
//get all orders
    public List<Order> getAllOrders(){
        return null;
    }
    //get order by id
    public Order getOrderById(){
        return null;
    }

}


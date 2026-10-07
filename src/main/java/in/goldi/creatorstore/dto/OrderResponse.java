package in.goldi.creatorstore.dto;

import in.goldi.creatorstore.entities.OrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

    private Long id;

    private String customerName;

    private String customerEmail;

    private OrderStatus status;

    private BigDecimal totalPrice;

    private LocalDateTime createdAt;

    private List<OrderItemResponse> items;
}
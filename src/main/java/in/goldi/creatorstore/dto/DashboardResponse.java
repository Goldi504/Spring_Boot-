package in.goldi.creatorstore.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardResponse {

    private long totalCustomers;

    private long totalProducts;

    private long totalOrders;

    private BigDecimal totalSales;

    private BigDecimal todaySales;

    private long lowStockProducts;
}
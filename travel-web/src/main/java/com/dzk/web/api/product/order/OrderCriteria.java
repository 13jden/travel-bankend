package com.dzk.web.api.product.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "订单搜索条件")
public class OrderCriteria {

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "产品ID")
    private Long productId;

    @Schema(description = "订单状态")
    private String status;
} 
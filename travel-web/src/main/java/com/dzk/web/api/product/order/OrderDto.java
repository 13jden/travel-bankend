package com.dzk.web.api.product.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

    @Schema(description = "订单id")
    private Long id;

    @Schema(description = "订单金额")
    private BigDecimal orderPrice;

    @Schema(description = "订单状态")
    private String status;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "文创产品id")
    private Long productId;

    @Schema(description = "数量")
    private Integer quantity;

    @Schema(description = "地址")
    private String address;

    @Schema(description = "联系人")
    private String contact;

    @Schema(description = "联系电话")
    private String phone;

    @Data
    @Schema(description = "订单输入数据传输对象")
    public static class Input {
        @Schema(description = "订单金额")
        private BigDecimal orderPrice;

        @Schema(description = "订单状态")
        private String status;

        @Schema(description = "用户id")
        private Long userId;

        @Schema(description = "文创产品id")
        private Long productId;

        @Schema(description = "数量")
        private Integer quantity;

        @Schema(description = "地址")
        private String address;

        @Schema(description = "联系人")
        private String contact;
        
        @Schema(description = "联系电话")
        private String phone;
    }

}


package com.dzk.web.product.order;

import lombok.Data;

import com.dzk.common.common.BaseEntity;

@Data
public class Order extends BaseEntity {

    @TableId(type = IdType.AUTO)
    @Schema(description = "订单id")
    private Long id;

    @TableField(value = "user_id")
    @Schema(description = "用户id")
    private String userId;
    
    @TableField(value = "product_id")
    @Schema(description = "文创产品id")
    private String productId;

    @TableField(value = "quantity")
    @Schema(description = "数量")
    private Integer quantity;
    
    @TableField(value = "order_price")
    @Schema(description = "订单金额")
    private BigDecimal orderPrice;

    @TableField(value = "address")
    @Schema(description = "地址")
    private String address;

    @TableField(value = "contact")
    @Schema(description = "联系人")
    private String contact;
    
    @TableField(value = "phone")
    @Schema(description = "联系电话")
    private String phone;

    @TableField(value = "status")
    @Schema(description = "状态")
    private OrderStatus status;
    
    enum OrderStatus {
        PENDING, // 待付款
        PAID, // 已付款
        SHIPPED, // 已发货
        NOT_SHIPPED, // 未发货
        COMPLETED, // 已完成
        CANCELLED // 已取消
        REFUNDED // 已退款
    }
}   
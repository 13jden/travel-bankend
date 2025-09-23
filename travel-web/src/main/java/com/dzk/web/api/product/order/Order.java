package com.dzk.web.api.product.order;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import com.dzk.common.common.BaseEntity;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("product_order")
public class Order extends BaseEntity {

    @TableId(type = IdType.AUTO)
    @Schema(description = "订单id")
    private Long id;

    @TableField(value = "user_id")
    @Schema(description = "用户id")
    private Long userId;
    
    @TableField(value = "product_id")
    @Schema(description = "文创产品id")
    private Long productId;

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
    private String status;
    
    @TableField(value = "remark")
    @Schema(description = "备注")
    private String remark;
}   
package com.dzk.web.api.product.order;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderConverter {

    /**
     * 实体转DTO
     */
    public static OrderDto toDto(Order order) {
        if (order == null) {
            return null;
        }
        
        return OrderDto.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .productId(order.getProductId())
                .quantity(order.getQuantity())
                .orderPrice(order.getOrderPrice())
                .address(order.getAddress())
                .contact(order.getContact())
                .phone(order.getPhone())
                .status(order.getStatus())
                .remark(order.getRemark())
                .build();
    }
    
    /**
     * 实体列表转DTO列表
     */
    public static List<OrderDto> toDtoList(List<Order> orders) {
        if (orders == null) {
            return null;
        }
        
        return orders.stream()
                .map(OrderConverter::toDto)
                .collect(Collectors.toList());
    }
    
    /**
     * 输入DTO转实体
     */
    public static Order toEntity(OrderDto.Input input) {
        if (input == null) {
            return null;
        }
        
        return Order.builder()
                .userId(input.getUserId())
                .productId(input.getProductId())
                .quantity(input.getQuantity())
                .orderPrice(input.getOrderPrice())
                .address(input.getAddress())
                .contact(input.getContact())
                .phone(input.getPhone())
                .status(input.getStatus())
                .remark(input.getRemark())
                .build();
    }
}
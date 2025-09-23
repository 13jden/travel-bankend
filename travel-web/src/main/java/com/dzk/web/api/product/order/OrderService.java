package com.dzk.web.api.product.order;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzk.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService extends ServiceImpl<OrderMapper, Order> {

    @Autowired
    OrderMapper orderMapper;

    /**
     * 搜索订单
     */
    public List<OrderDto> search(OrderCriteria criteria, Pageable pageable) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        
        if (criteria.getUserId() != null) {
            queryWrapper.eq("user_id", criteria.getUserId());
        }
        if (criteria.getProductId() != null) {
            queryWrapper.eq("product_id", criteria.getProductId());
        }
        if (criteria.getStatus() != null && !criteria.getStatus().isEmpty()) {
            queryWrapper.eq("status", criteria.getStatus());
        }
        queryWrapper.orderByDesc("create_time");
        
        Page<Order> page = new Page<>(pageable.getPageNumber() + 1, pageable.getPageSize());
        Page<Order> orderPage = this.page(page, queryWrapper);
        
        return OrderConverter.toDtoList(orderPage.getRecords());
    }

    /**
     * 添加订单
     */
    @Transactional
    public OrderDto add(OrderDto.Input input) {
        try {
            Order order = OrderConverter.toEntity(input);
            orderMapper.insert(order);
            return OrderConverter.toDto(order);
        } catch (Exception e) {
            throw new BusinessException("添加订单失败: " + e.getMessage());
        }
    }

    /**
     * 更新订单
     */
    @Transactional
    public OrderDto update(Long id, OrderDto.Input input) {
        try {
            Order existingOrder = orderMapper.selectById(id);
            if (existingOrder == null) {
                throw new BusinessException("订单不存在！");
            }
            
            Order order = OrderConverter.toEntity(input);
            order.setId(id);
            orderMapper.updateById(order);
            
            return OrderConverter.toDto(order);
        } catch (Exception e) {
            throw new BusinessException("更新订单失败: " + e.getMessage());
        }
    }

    /**
     * 获取订单详情
     */
    public OrderDto getDetail(Long id) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        return OrderConverter.toDto(order);
    }

    /**
     * 删除订单
     */
    @Transactional
    public OrderDto delete(Long id) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        try {
            orderMapper.deleteById(id);
            return OrderConverter.toDto(order);
        } catch (Exception e) {
            throw new BusinessException("删除订单失败: " + e.getMessage());
        }
    }

    /**
     * 更新订单状态
     */
    @Transactional
    public OrderDto updateStatus(Long id, String status) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        order.setStatus(status);
        orderMapper.updateById(order);
        
        return OrderConverter.toDto(order);
    }
}
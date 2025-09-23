package com.dzk.admin.api.product.order;

import com.dzk.common.common.Result;
import com.dzk.web.api.product.order.OrderCriteria;
import com.dzk.web.api.product.order.OrderDto;
import com.dzk.web.api.product.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("order")
@Tag(name = "订单管理")
public class OrderManagerController {

    @Autowired
    OrderService orderService;

    @GetMapping("{id}")
    @Operation(summary = "获取订单详情", description = "获取订单详情")
    public Result<OrderDto> getDetail(@PathVariable Long id){
        return Result.success(orderService.getDetail(id));
    }

    @PostMapping("")
    @Operation(summary = "添加订单", description = "添加订单")
    public Result<OrderDto> add(@RequestBody OrderDto.Input input){
        return Result.success(orderService.add(input));
    }

    @DeleteMapping("{id}")
    @Operation(summary = "删除订单", description = "删除订单")
    public Result<OrderDto> delete(@PathVariable Long id){
        return Result.success(orderService.delete(id));
    }

    @PutMapping("{id}")
    @Operation(summary = "更新订单", description = "更新订单")
    public Result<OrderDto> update(@PathVariable Long id, @RequestBody OrderDto.Input input){
        return Result.success(orderService.update(id, input));
    }

    @GetMapping("search")
    @Operation(summary = "搜索订单", description = "搜索订单")
    public Result<List<OrderDto>> search(OrderCriteria criteria, Pageable pageable){
        return Result.success(orderService.search(criteria, pageable));
    }

    @PutMapping("{id}/status")
    @Operation(summary = "更新订单状态", description = "更新订单状态")
    public Result<OrderDto> updateStatus(@PathVariable Long id, @RequestParam String status){
        return Result.success(orderService.updateStatus(id, status));
    }
}

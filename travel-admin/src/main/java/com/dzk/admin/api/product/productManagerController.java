package com.dzk.admin.api.product;

import com.dzk.common.common.Result;
import com.dzk.web.api.product.Product;
import com.dzk.web.api.product.ProductCriteria;
import com.dzk.web.api.product.ProductDto;
import com.dzk.web.api.product.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("product")
@Tag(name = "产品管理")
public class ProductManagerController {

    @Autowired
    ProductService productService;

    @GetMapping("{id}")
    @Operation(summary = "获取产品详情", description = "获取产品详情")
    public Result<ProductDto> getDetail(@PathVariable Long id){
        return Result.success(productService.getDetail(id));
    }

    @PostMapping("")
    @Operation(summary = "添加产品", description = "添加产品")
    public Result<ProductDto> add(@RequestBody ProductDto.Input input){
        return Result.success(productService.add(input));
    }

    @DeleteMapping("{id}")
    @Operation(summary = "删除产品", description = "删除产品")
    public Result<ProductDto> delete(@PathVariable Long id){
        return Result.success(productService.delete(id));
    }

    @PutMapping("{id}")
    @Operation(summary = "更新产品", description = "更新产品")
    public Result<ProductDto> update(@PathVariable Long id, @RequestBody ProductDto.Input input){
        return Result.success(productService.update(id, input));
    }

    @GetMapping("search")
    @Operation(summary = "搜索产品", description = "搜索产品")
    public Result<List<ProductDto>> search(ProductCriteria criteria, Pageable pageable){
        return Result.success(productService.search(criteria, pageable));
    }

    @DeleteMapping("batch")
    @Operation(summary = "批量删除产品", description = "批量删除产品")
    public Result deleteBatch(@RequestBody List<Long> ids){
        productService.deleteBatch(ids);
        return Result.success("ok");
    }
}

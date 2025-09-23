package com.dzk.admin.api.product.category;

import com.dzk.common.common.Result;
import com.dzk.web.api.product.category.CategoryDto;
import com.dzk.web.api.product.category.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("product/category")
@Tag(name = "产品分类管理")
public class ProductCategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping("{id}")
    @Operation(summary = "获取分类详情", description = "获取分类详情")
    public Result<CategoryDto> getDetail(@PathVariable Long id){
        return Result.success(categoryService.getDetail(id));
    }

    @PostMapping("")
    @Operation(summary = "添加分类", description = "添加分类")
    public Result<CategoryDto> add(@RequestBody CategoryDto.Input input){
        return Result.success(categoryService.add(input));
    }

    @DeleteMapping("{id}")
    @Operation(summary = "删除分类", description = "删除分类")
    public Result<CategoryDto> delete(@PathVariable Long id){
        return Result.success(categoryService.delete(id));
    }

    @PutMapping("{id}")
    @Operation(summary = "更新分类", description = "更新分类")
    public Result<CategoryDto> update(@PathVariable Long id, @RequestBody CategoryDto.Input input){
        return Result.success(categoryService.update(id, input));
    }

    @GetMapping("list")
    @Operation(summary = "获取分类列表", description = "获取分类列表")
    public Result<List<CategoryDto>> list(){
        return Result.success(categoryService.getList());
    }
}
package com.dzk.admin.api.attraction;

import com.dzk.common.common.Result;
import com.dzk.web.api.attraction.Attraction;
import com.dzk.web.api.attraction.AttractionCriteria;
import com.dzk.web.api.attraction.AttractionDto;
import com.dzk.web.api.attraction.AttractionService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("attraction")
@Tag(name = "景点管理")
public class AttractionManagerController {

    @Autowired
    AttractionService attractionService;

    @GetMapping("{id}")
    @Operation(summary = "获取景点详情", description = "获取景点详情")
    public Result<AttractionDto> getDetail(@PathVariable Long id){
        return Result.success(attractionService.getDetail(id));
    }

    @PostMapping("")
    @Operation(summary = "添加景点", description = "添加景点")
    public Result<AttractionDto> add(@RequestBody AttractionDto.Input input){
        return Result.success(attractionService.add(input));
    }

    @DeleteMapping("{id}")
    @Operation(summary = "删除景点", description = "删除景点")
    public Result<AttractionDto> delete(@PathVariable Long id){
        return Result.success(attractionService.delete(id));
    }

    @PutMapping("{id}")
    @Operation(summary = "更新景点", description = "更新景点")
    public Result<AttractionDto> update(@PathVariable Long id,@RequestBody AttractionDto.Input input){
        return Result.success(attractionService.update(id,input));
    }

    @GetMapping("search")
    @Operation(summary = "搜索景点", description = "搜索景点")
    public Result<List<AttractionDto>> getDetail(AttractionCriteria criteria, Pageable pageable){
        return Result.success(attractionService.search(criteria,pageable));
    }
}
package com.dzk.admin.api.attraction;

import com.dzk.common.common.Result;
import com.dzk.web.api.attraction.Attraction;
import com.dzk.web.api.attraction.AttractionCriteria;
import com.dzk.web.api.attraction.AttractionDto;
import com.dzk.web.api.attraction.AttractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("attraction")
public class AttractionManagerController {

    @Autowired
    AttractionService attractionService;

    @GetMapping("{id}")
    public Result<AttractionDto> getDetail(@PathVariable Long id){
        return Result.success(attractionService.getDetail(id));
    }

    @PostMapping("")
    public Result<AttractionDto> add(@RequestBody AttractionDto.Input input){
        return Result.success(attractionService.add(input));
    }

    @DeleteMapping("{id}")
    public Result<AttractionDto> delete(@PathVariable Long id){
        return Result.success(attractionService.delete(id));
    }

    @PutMapping("{id}")
    public Result<AttractionDto> update(@PathVariable Long id,@RequestBody AttractionDto.Input input){
        return Result.success(attractionService.update(id,input));
    }

    @GetMapping("search")
    public Result<List<AttractionDto>> getDetail(AttractionCriteria criteria, Pageable pageable){
        return Result.success(attractionService.search(criteria,pageable));
    }
}
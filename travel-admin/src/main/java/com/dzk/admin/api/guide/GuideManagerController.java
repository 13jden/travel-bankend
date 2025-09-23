package com.dzk.admin.api.guide;

import com.dzk.common.common.Result;
import com.dzk.web.api.guide.Guide;
import com.dzk.web.api.guide.GuideCriteria;
import com.dzk.web.api.guide.GuideDto;
import com.dzk.web.api.guide.GuideService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("guide")
@Tag(name = "攻略管理")
public class GuideManagerController {

    @Autowired
    GuideService guideService;

    @GetMapping("{id}")
    @Operation(summary = "获取攻略详情", description = "获取攻略详情")
    public Result<GuideDto> getDetail(@PathVariable Long id){
        return Result.success(guideService.getDetail(id));
    }

    @PostMapping("")
    @Operation(summary = "添加攻略", description = "添加攻略")
    public Result<GuideDto> add(@RequestBody GuideDto.Input input){
        return Result.success(guideService.add(input));
    }

    @DeleteMapping("{id}")
    @Operation(summary = "删除攻略", description = "删除攻略")
    public Result<GuideDto> delete(@PathVariable Long id){
        return Result.success(guideService.delete(id));
    }

    @PutMapping("{id}")
    @Operation(summary = "更新攻略", description = "更新攻略")
    public Result<GuideDto> update(@PathVariable Long id, @RequestBody GuideDto.Input input){
        return Result.success(guideService.update(id, input));
    }

    @GetMapping("search")
    @Operation(summary = "搜索攻略", description = "搜索攻略")
    public Result<List<GuideDto>> search(GuideCriteria criteria, Pageable pageable){
        return Result.success(guideService.search(criteria, pageable));
    }

    @DeleteMapping("batch")
    @Operation(summary = "批量删除攻略", description = "批量删除攻略")
    public Result deleteBatch(@RequestBody List<Long> ids){
        guideService.deleteBatch(ids);
        return Result.success();
    }

    @PostMapping("{id}/click")
    @Operation(summary = "增加点击量", description = "增加点击量")
    public Result incrementClick(@PathVariable Long id){
        guideService.incrementClickCount(id);
        return Result.success();
    }

    @PostMapping("{id}/like")
    @Operation(summary = "增加点赞量", description = "增加点赞量")
    public Result incrementLike(@PathVariable Long id){
        guideService.incrementLikeCount(id);
        return Result.success();
    }

    @PostMapping("{id}/collect")
    @Operation(summary = "增加收藏量", description = "增加收藏量")
    public Result incrementCollect(@PathVariable Long id){
        guideService.incrementCollectCount(id);
        return Result.success();
    }

    @GetMapping("{id}/markdown")
    @Operation(summary = "获取markdown内容", description = "获取markdown文件内容")
    public Result<String> getMarkdownContent(@PathVariable Long id){
        return Result.success(guideService.getMarkdownContent(id));
    }
}

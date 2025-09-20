package com.dzk.admin.api.banner;

import com.dzk.common.common.Result;
import com.dzk.web.api.banner.BannerService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Map;

@RestController
@Tag(name = "BannerManagerController", description = "Banner管理")
public class BannerManagerController {

    @Autowired
    private BannerService bannerService;

    @GetMapping("/getList")
    public Result getList() {
        return Result.success();
    }

        /**
     * 添加Banner
     *
     * @param image     图片
     * @param type      类型：1为场地，2为商品，3为笔记
     * @param isActive        是否启用：1是，0否
     * @param text      标题（可选）
     * @param contentId 跳转ID（可选）
     * @return 添加结果
     */
    @RequestMapping("/add")
    public Result add(
            @NotEmpty String image,
            @NotNull Integer type,
            Integer isActive,
            String text,
            Integer contentId) {
        System.out.println("添加");
        return Result.success();
    }

    /**
     * 更新Banner
     *
     * @param bannerId  Banner ID
     * @param image     图片
     * @param type      类型：1为场地，2为商品，3为笔记
     * @param isActive        是否启用：1是，0否
     * @param text      标题（可选）
     * @param contentId 跳转ID（可选）
     * @return 更新结果
     */
    @RequestMapping("/update")
    public Result update(
            @NotNull Integer bannerId,
            String image,
            Integer type,
            Integer isActive,
            String text,
            Integer contentId) {
        return Result.success();
    }

    /**
     * 删除Banner
     *
     * @param bannerId Banner ID
     * @return 删除结果
     */
    @RequestMapping("/delete")
    public Result delete(@NotNull Integer bannerId) {
        return Result.success();
    }

    /**
     * 更新Banner排序
     *
     * @param sortList 排序列表，包含bannerId和sort
     * @return 更新结果
     */
    @RequestMapping("/sort")
    public Result sort(@RequestBody @NotEmpty List<Map<String, Integer>> sortList) {
        return Result.success();
    }
}
package com.dzk.web.api.banner;

import com.dzk.common.common.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/banner")
@Tag(name = "BannerController", description = "Banner查看")
public class BannerController {

    @Autowired
    private BannerService bannerService;

    /**
     * 获取Banner列表
     *
     * @return Banner列表
     */
    @RequestMapping("/getList")
    public Result getList() {
        return Result.success(bannerService.getList());
    }

}

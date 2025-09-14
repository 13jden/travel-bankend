package com.dzk.web.api.banner;

import com.dzk.common.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/banner")
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

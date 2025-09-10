package com.dzk.admin.api.banner;

import com.dzk.common.common.Result;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

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

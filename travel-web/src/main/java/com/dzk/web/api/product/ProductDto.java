package com.dzk.web.api.product;    

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

public class ProductDto {
    

    @Data
    @Schema(description = "产品输入对象")
    public class ProductInput {

        @Schema(description = "产品标题")
        private String title;
    }
}   
package com.dzk.web.api.auth.captcha;

import lombok.Data;

@Data
public class CaptchaCode {

    /**
     * 验证码配置
     */
    private CaptchaCodeType codeType = CaptchaCodeType.ARITHMETIC;
    /**
     * 验证码有效期 分钟
     */
    private Long expiration = 3L;

    /**
     * 验证码内容长度
     */
    private int length = 4;
    /**
     * 验证码宽度
     */
    private int width = 111;
    /**
     * 验证码高度
     */
    private int height = 36;
    /**
     * 验证码字体
     */
    private String fontName;
    /**
     * 字体大小
     */
    private int fontSize = 25;

    public CaptchaCodeType getCodeType() {
        return codeType;
    }
}

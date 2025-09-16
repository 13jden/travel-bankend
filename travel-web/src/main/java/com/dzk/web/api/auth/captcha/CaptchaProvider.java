package com.dzk.web.api.auth.captcha;


import java.awt.Font;
import java.util.Objects;

import com.dzk.common.exception.BusinessException;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.ChineseCaptcha;
import com.wf.captcha.ChineseGifCaptcha;
import com.wf.captcha.GifCaptcha;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class CaptchaProvider {
    /**
     * 账号单用户 登录
     */
    private boolean singleLogin = false;

    private CaptchaCode captchaCode;

    /**
     * 邮箱验证码有效期 分钟
     */
    private Long emailExpiration = 5L;

    public static final String CACHE_KEY = "USER-LOGIN-DATA";

    public boolean isSingleLogin() {
        return singleLogin;
    }

    /**
     * 获取验证码生产类
     *
     * @return /
     */
    public Captcha getCaptcha() {
        if (Objects.isNull(captchaCode)) {
            captchaCode = new CaptchaCode();
            if (Objects.isNull(captchaCode.getCodeType())) {
                captchaCode.setCodeType(CaptchaCodeType.ARITHMETIC);
            }
        }
        return switchCaptcha(captchaCode);
    }

    /**
     * 依据配置信息生产验证码
     *
     * @param captchaCode 验证码配置信息
     * @return /
     */
    private Captcha switchCaptcha(CaptchaCode captchaCode) {
        Captcha captcha;
        switch (captchaCode.getCodeType()) {
            case ARITHMETIC:
                captcha = new FixedArithmeticCaptcha(captchaCode.getWidth(), captchaCode.getHeight());
                // 几位数运算，默认是两位
                captcha.setLen(captchaCode.getLength());
                break;
            case CHINESE:
                captcha = new ChineseCaptcha(captchaCode.getWidth(), captchaCode.getHeight());
                captcha.setLen(captchaCode.getLength());
                break;
            case CHINESE_GIF:
                captcha = new ChineseGifCaptcha(captchaCode.getWidth(), captchaCode.getHeight());
                captcha.setLen(captchaCode.getLength());
                break;
            case GIF:
                captcha = new GifCaptcha(captchaCode.getWidth(), captchaCode.getHeight());
                captcha.setLen(captchaCode.getLength());
                break;
            case SPEC:
                captcha = new SpecCaptcha(captchaCode.getWidth(), captchaCode.getHeight());
                captcha.setLen(captchaCode.getLength());
                break;
            default:
                throw new BusinessException("验证码配置信息错误！正确配置查看 LoginCodeEnum ");
        }
        if(StringUtils.isNotBlank(captchaCode.getFontName())){
            captcha.setFont(new Font(captchaCode.getFontName(), Font.PLAIN, captchaCode.getFontSize()));
        }
        return captcha;
    }

    static class FixedArithmeticCaptcha extends ArithmeticCaptcha {
        public FixedArithmeticCaptcha(int width, int height) {
            super(width, height);
        }

        @Override
        protected char[] alphas() {
            // 生成随机数字和运算符
            int n1 = num(1, 10);
			int n2 = num(1, 10);
            int opt = num(3);

            // 计算结果
            int res = new int[]{n1 + n2, n1 - n2, n1 * n2}[opt];
            // 转换为字符运算符
            var optChar = "+-x".charAt(opt);

            this.setArithmeticString(String.format("%s%c%s=?", n1, optChar, n2));
            this.chars = String.valueOf(res);

            return chars.toCharArray();
        }
    }
}

package com.dzk.web.user;

import com.dzk.common.redis.RedisComponent;
import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("captcha")
public class CaptchaController {

    @Autowired
    private Producer captchaProducer;
    
    @Autowired
    private RedisComponent redisComponent;

    /**
     * 生成验证码
     */
    @GetMapping("/generate")
    public Map<String, String> generateCaptcha() {
        // 生成验证码文本
        String captchaText = captchaProducer.createText();
        
        // 生成验证码图片
        BufferedImage captchaImage = captchaProducer.createImage(captchaText);
        
        // 生成唯一标识
        String captchaKey = UUID.randomUUID().toString();
        
        // 将验证码存储到Redis，设置1分钟过期
        redisComponent.saveCheckCode(captchaKey, captchaText);
        
        // 将图片转换为Base64字符串
        String base64Image = convertImageToBase64(captchaImage);
        
        Map<String, String> result = new HashMap<>();
        result.put("captchaKey", captchaKey);
        result.put("captchaImage", "data:image/jpeg;base64," + base64Image);
        
        return result;
    }
    
    /**
     * 验证验证码
     */
    @PostMapping("/verify")
    public Map<String, Object> verifyCaptcha(@RequestParam String captchaKey, @RequestParam String captchaCode) {
        Map<String, Object> result = new HashMap<>();
        
        // 从Redis获取验证码
        String storedCode = redisComponent.getCheckCode(captchaKey);
        
        if (storedCode != null && storedCode.equalsIgnoreCase(captchaCode)) {
            // 验证成功，删除验证码
            redisComponent.cleanCheckCode(captchaKey);
            result.put("success", true);
            result.put("message", "验证码验证成功");
        } else {
            result.put("success", false);
            result.put("message", "验证码错误或已过期");
        }
        
        return result;
    }
    
    /**
     * 将BufferedImage转换为Base64字符串
     */
    private String convertImageToBase64(BufferedImage image) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, "jpeg", baos);
            byte[] imageBytes = baos.toByteArray();
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            throw new RuntimeException("转换验证码图片失败", e);
        }
    }
} 
package com.dzk.web.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class WxLoginTool {

    // 微信小程序的 AppID 和 AppSecret
    private static final String APP_ID = "wxbd6e9f2ee1d7420d";
    private static final String APP_SECRET = "e47d4183aca1f20604992906d6c38014";

    // 微信登录接口地址
    private static final String WX_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";
    
    // 微信获取access_token接口地址
    private static final String WX_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";
    
    // 微信发送订阅消息接口地址
    private static final String WX_SUBSCRIBE_MESSAGE_URL = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send";
    
    // 订阅消息模板ID
    private static final String TEMPLATE_ID = "4OFcdPl680DgcRzmHDs2Jh-DQCyYlkZ2vRfXZ3-ENCk";

    // HTTP 客户端
    private final OkHttpClient httpClient = new OkHttpClient();
    
    // JSON解析器
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 处理微信登录请求
     *
     * @param code 小程序端传来的 code
     * @return 返回 openid 和 session_key
     */
    public String wxLogin(String code) {
        // 构造请求 URL
        String url = String.format("%s?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                WX_LOGIN_URL, APP_ID, APP_SECRET, code);

        // 发送 HTTP 请求
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                // 获取微信服务器的响应
                String responseBody = response.body().string();
                System.out.println("微信服务器响应: " + responseBody);

                // 解析 JSON 响应
                Map<String, Object> responseMap = objectMapper.readValue(responseBody, Map.class);

                // 检查是否有错误
                if (responseMap.containsKey("errcode")) {
                    Integer errcode = (Integer) responseMap.get("errcode");
                    String errmsg = (String) responseMap.get("errmsg");
                    System.out.println("微信登录错误: errcode=" + errcode + ", errmsg=" + errmsg);
                    throw new RuntimeException("微信登录失败: " + errmsg);
                }
                
                // 提取 openid
                String openid = (String) responseMap.get("openid");
                if (openid != null) {
                    return openid; // 返回 openid
                } else {
                    throw new RuntimeException("微信登录失败：未获取到openid");
                }
            } else {
                System.out.println("请求微信服务器失败，状态码: " + response.code());
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("请求微信服务器时发生异常: " + e.getMessage());
            return null;
        }
    }

    /**
     * 获取微信access_token
     *
     * @return access_token字符串
     */
    public String getAccessToken() {
        String url = String.format("%s?grant_type=client_credential&appid=%s&secret=%s",
                WX_ACCESS_TOKEN_URL, APP_ID, APP_SECRET);

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                System.out.println("获取access_token响应: " + responseBody);

                Map<String, Object> responseMap = objectMapper.readValue(responseBody, Map.class);

                // 检查是否有错误
                if (responseMap.containsKey("errcode")) {
                    Integer errcode = (Integer) responseMap.get("errcode");
                    String errmsg = (String) responseMap.get("errmsg");
                    System.out.println("获取access_token错误: errcode=" + errcode + ", errmsg=" + errmsg);
                    throw new RuntimeException("获取access_token失败: " + errmsg);
                }

                String accessToken = (String) responseMap.get("access_token");
                if (accessToken != null) {
                    return accessToken;
                } else {
                    throw new RuntimeException("获取access_token失败：未获取到access_token");
                }
            } else {
                System.out.println("请求微信服务器失败，状态码: " + response.code());
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("请求微信服务器时发生异常: " + e.getMessage());
            return null;
        }
    }

    /**
     * 发送订阅消息给用户
     *
     * @param openId 用户的openId
     * @param activityName 活动名称（如：早餐饮食打卡、午餐饮食打卡、晚餐饮食打卡）
     * @param timeRange 时间段（如：08:00~10:00、12:00~14:00、18:00~20:00）
     * @return 是否发送成功
     */
    public boolean sendSubscribeMessage(String openId, String activityName, String timeRange) {
        try {
            // 获取access_token
            String accessToken = getAccessToken();
            if (accessToken == null) {
                System.out.println("获取access_token失败，无法发送订阅消息");
                return false;
            }

            // 构造请求URL
            String url = String.format("%s?access_token=%s", WX_SUBSCRIBE_MESSAGE_URL, accessToken);

            // 构造请求体
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("touser", openId);
            requestBody.put("template_id", TEMPLATE_ID);
            requestBody.put("page", "pages/index/index"); // 点击消息后跳转的页面
            requestBody.put("miniprogram_state", "formal"); // 正式版
            requestBody.put("lang", "zh_CN"); // 中文

            // 构造模板数据
            Map<String, Object> data = new HashMap<>();
            
            // 活动名称
            Map<String, String> thing1 = new HashMap<>();
            thing1.put("value", activityName);
            data.put("thing1", thing1);
            
            // 时间段
            Map<String, String> time7 = new HashMap<>();
            time7.put("value", timeRange);
            data.put("time7", time7);
            
            requestBody.put("data", data);

            // 发送请求
            String jsonBody = objectMapper.writeValueAsString(requestBody);
            RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json; charset=utf-8"));
            
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    System.out.println("发送订阅消息响应: " + responseBody);

                    Map<String, Object> responseMap = objectMapper.readValue(responseBody, Map.class);

                    // 检查是否有错误
                    if (responseMap.containsKey("errcode")) {
                        Integer errcode = (Integer) responseMap.get("errcode");
                        String errmsg = (String) responseMap.get("errmsg");
                        System.out.println("发送订阅消息错误: errcode=" + errcode + ", errmsg=" + errmsg);
                        return false;
                    }

                    System.out.println("订阅消息发送成功，openId: " + openId);
                    return true;
                } else {
                    System.out.println("发送订阅消息失败，状态码: " + response.code());
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("发送订阅消息时发生异常: " + e.getMessage());
            return false;
        }
    }
}
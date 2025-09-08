package com.dzk.web.Agent;

import com.alibaba.dashscope.app.Application;
import com.alibaba.dashscope.app.ApplicationParam;
import com.alibaba.dashscope.app.ApplicationResult;
import com.alibaba.dashscope.app.ApplicationOutput;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import io.reactivex.Flowable;

import java.util.HashMap;
import java.util.Map;

@Service
public class ChatService {

     @Value("${spring.ai.dashscope.agent.app-id}")
     String appId;
     @Value("${spring.ai.dashscope.api-key}")
     String apiKey;

    public Flux<String> streamChat(String prompt) {
        Map<String, String> bizParamMap = new HashMap<>();
        bizParamMap.put("name", "Alice");
        bizParamMap.put("age", "30");

        // 正确地将 Map 转换为 JsonObject
        JsonObject bizParams = new JsonObject();
        for (Map.Entry<String, String> entry : bizParamMap.entrySet()) {
            bizParams.addProperty(entry.getKey(), entry.getValue());
        }

        ApplicationParam param = ApplicationParam.builder()
                .apiKey(apiKey)
                .appId(appId)
                .prompt(prompt)
                .bizParams(bizParams)  // 使用正确的 JsonObject
                .incrementalOutput(true)
                .hasThoughts(true)
                .build();

        Application application = new Application();
        Flowable<ApplicationResult> resultFlowable;
        try {
            resultFlowable = application.streamCall(param);
        } catch (NoApiKeyException | InputRequiredException e) {
            throw new RuntimeException(e);
        }

        return Flux.from(resultFlowable)
                .map(result -> {
                    ApplicationOutput output = result.getOutput();
                    if (output != null && output.getText() != null) {
                        return output.getText();
                    }
                    return "";
                });
    }

//    // 控制台测试
//    public static void main(String[] args) {
//        ChatService chatService = new ChatService();
//        java.util.Scanner scanner = new java.util.Scanner(System.in);
//        System.out.println("请输入你的问题（输入exit退出）：");
//        while (true) {
//            System.out.print("你: ");
//            String input = scanner.nextLine();
//            if ("exit".equalsIgnoreCase(input)) break;
//
//            System.out.print("AI: ");
//            chatService.streamChat(input)
//                    .doOnNext(s -> System.out.print(s))
//                    .blockLast();
//            System.out.println();
//        }
//        scanner.close();
//        System.out.println("对话结束");
//    }
}

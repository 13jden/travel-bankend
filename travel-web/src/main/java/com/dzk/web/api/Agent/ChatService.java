package com.dzk.web.api.Agent;

import com.alibaba.dashscope.app.Application;
import com.alibaba.dashscope.app.ApplicationOutput;
import com.alibaba.dashscope.app.ApplicationParam;
import com.alibaba.dashscope.app.ApplicationResult;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.reactivex.Flowable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class ChatService {

     @Value("${spring.ai.dashscope.agent.app-id}")
     String appId;
     @Value("${spring.ai.dashscope.api-key}")
     String apiKey;
     
     @Autowired
     private Gson gson;

     
     // 创建支持 LocalDate 和 File 的 Gson 实例
     private static Gson createGsonWithLocalDateSupport() {
         return new GsonBuilder()
                 .registerTypeAdapter(LocalDate.class, new TypeAdapter<LocalDate>() {
                     private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
                     
                     @Override
                     public void write(JsonWriter out, LocalDate value) throws IOException {
                         if (value == null) {
                             out.nullValue();
                         } else {
                             out.value(formatter.format(value));
                         }
                     }
                     
                     @Override
                     public LocalDate read(JsonReader in) throws IOException {
                         if (in.peek() == com.google.gson.stream.JsonToken.NULL) {
                             in.nextNull();
                             return null;
                         }
                         return LocalDate.parse(in.nextString(), formatter);
                     }
                 })
                 .registerTypeAdapter(File.class, new TypeAdapter<File>() {
                     @Override
                     public void write(JsonWriter out, File value) throws IOException {
                         if (value == null) {
                             out.nullValue();
                         } else {
                             // 只序列化文件的路径，而不是整个 File 对象
                             out.value(value.getAbsolutePath());
                         }
                     }
                     
                     @Override
                     public File read(JsonReader in) throws IOException {
                         if (in.peek() == com.google.gson.stream.JsonToken.NULL) {
                             in.nextNull();
                             return null;
                         }
                         String path = in.nextString();
                         return new File(path);
                     }
                 })
                 .create();
     }

    public Flux<String> streamChat(String type, ChatRequest request) {
        try {
            System.out.println("开始处理聊天请求，type: " + type);
            System.out.println("请求内容: " + (request != null ? request.getContent() : "null"));
            //todo 加入相关景点，文创信息。

            Map<String, String> bizParamMap = new HashMap<>();
            // 将请求对象序列化为 JSON 字符串放入 content，若为空则放空字符串
            // 使用支持 LocalDate 的 Gson 实例
            Gson gsonWithLocalDate = createGsonWithLocalDateSupport();
            String contentJson = request != null ? gsonWithLocalDate.toJson(request) : "";
            String typeValue = type != null ? type : "";
            bizParamMap.put("type", typeValue);
            bizParamMap.put("content", contentJson);


            System.out.println("业务参数: " + bizParamMap);

            // 正确地将 Map 转换为 JsonObject
            JsonObject bizParams = new JsonObject();
            for (Map.Entry<String, String> entry : bizParamMap.entrySet()) {
                bizParams.addProperty(entry.getKey(), entry.getValue());
            }

            String prompt = (request != null && request.getContent() != null) ? request.getContent() : "";
            System.out.println("提示词: " + prompt);
            System.out.println("App ID: " + appId);
            System.out.println("API Key: " + (apiKey != null ? "已设置" : "未设置"));

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
                System.out.println("成功创建流式调用");
            } catch (NoApiKeyException | InputRequiredException e) {
                System.err.println("API调用异常: " + e.getMessage());
                throw new RuntimeException(e);
            }

            return Flux.from(resultFlowable)
                    .doOnNext(result -> System.out.println("收到结果: " + result))
                    .map(result -> {
                        ApplicationOutput output = result.getOutput();
                        if (output != null) {
                            // 流式过程中从"智能咨询"节点提取文本
                            if (output.getThoughts() != null) {
                                for (ApplicationOutput.Thought thought : output.getThoughts()) {
                                    if (thought.getResponse() != null) {
                                        try {
                                            JsonObject responseJson = gson.fromJson(thought.getResponse(), JsonObject.class);
                                            String nodeName = responseJson.get("nodeName") != null ? 
                                                responseJson.get("nodeName").getAsString() : "";
                                            String nodeType = responseJson.get("nodeType") != null ? 
                                                responseJson.get("nodeType").getAsString() : "";
                                            
                                            // 专门从"智能咨询"LLM节点提取流式文本
                                            if ("智能咨询".equals(nodeName) && "LLM".equals(nodeType)) {
                                                String nodeResult = responseJson.get("nodeResult") != null ? 
                                                    responseJson.get("nodeResult").getAsString() : "";
                                                if (!nodeResult.isEmpty()) {
                                                    // 解析 nodeResult 中的 result 字段
                                                    JsonObject nodeResultJson = gson.fromJson(nodeResult, JsonObject.class);
                                                    String resultText = nodeResultJson.get("result") != null ? 
                                                        nodeResultJson.get("result").getAsString() : "";
                                                    if (!resultText.isEmpty()) {
                                                        System.out.println("智能咨询流式文本: " + resultText);
                                                        return resultText;
                                                    }
                                                }
                                            }
                                        } catch (Exception e) {
                                            // 忽略解析错误，继续处理其他节点
                                        }
                                    }
                                }
                            }
                        }
                        return "";
                    })
                    .filter(s -> s != null && !s.isEmpty())
                    .doOnError(error -> System.err.println("流处理错误: " + error.getMessage()))
                    .doOnComplete(() -> System.out.println("流处理完成"));
        } catch (Exception e) {
            System.err.println("streamChat 方法异常: " + e.getMessage());
            e.printStackTrace();
            return Flux.error(e);
        }
    }

    // 控制台测试
//    public static void main(String[] args) {
//        // 创建测试用的 ChatService 实例
//        ChatService chatService = new ChatService();
//        // 手动设置配置（测试用）
//        chatService.appId = "be899b8713fd4042b1f54b1f7dcca20e"; // 请替换为实际的 App ID
//        chatService.apiKey = "sk-c048c72e74024e53a1fe0248b8213f6a"; // 请替换为实际的 API Key
//        chatService.gson = createGsonWithLocalDateSupport();
//
//        java.util.Scanner scanner = new java.util.Scanner(System.in);
//        System.out.println("请输入你的问题（输入exit退出）：");
//
//        while (true) {
//            System.out.print("你: ");
//            String input = scanner.nextLine();
//            if ("exit".equalsIgnoreCase(input)) break;
//
//            // 创建测试请求
//            ChatRequest request = new ChatRequest();
//            request.setContent(input);
//
//            System.out.print("AI: ");
//            try {
//                chatService.streamChat("text", request)
//                        .doOnNext(s -> System.out.print(s))
//                        .doOnError(error -> System.err.println("错误: " + error.getMessage()))
//                        .blockLast();
//            } catch (Exception e) {
//                System.err.println("测试异常: " + e.getMessage());
//                e.printStackTrace();
//            }
//            System.out.println();
//        }
//        scanner.close();
//        System.out.println("对话结束");
//    }
}

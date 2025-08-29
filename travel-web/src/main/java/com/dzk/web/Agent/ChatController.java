package com.dzk.web.Agent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("chat")
public class ChatController {
    @Autowired
    private ChatService chatService;

    @GetMapping("/stream")
    public Flux<String> stream(@RequestParam(value = "message", defaultValue = "你好，请问你的知识库文档主要是关于什么内容的?") String message) {
        return chatService.streamChat(message);
    }
}

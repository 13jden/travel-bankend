package com.dzk.web.api.Agent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("chat")
public class ChatController {
    @Autowired
    private ChatService chatService;

    @PostMapping("/stream/{type}")
    public Flux<String> stream(@PathVariable String type, @RequestBody ChatRequest request) {
        return chatService.streamChat(type, request);
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }
}

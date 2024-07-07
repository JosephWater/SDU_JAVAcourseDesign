package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.service.GeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    @Autowired
    private GeminiService geminiService;

    @PostMapping("/chat")
    public String chat(@RequestBody String prompt) {
        return geminiService.getChatResponse(prompt);
    }
}

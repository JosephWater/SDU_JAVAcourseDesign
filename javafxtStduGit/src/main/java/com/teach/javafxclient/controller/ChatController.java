package com.teach.javafxclient.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
//import com.example.chatbot.service.GeminiService;
//import org.fatmansoft.teach.service;
@Component
public class ChatController {

   /* @Autowired
    private GeminiService geminiService;*/

    @FXML
    private TextArea chatArea;

    @FXML
    private TextField inputField;

    @FXML
    public void handleSendAction() {
        String userInput = inputField.getText();
        if (!userInput.isEmpty()) {
            // 调用Spring Boot后端API获取回复
            //String response = geminiService.getChatResponse(userInput);
            chatArea.appendText("You: " + userInput + "\n");
           // chatArea.appendText("Gemini: " + response + "\n");
            inputField.clear();
        }
    }
}

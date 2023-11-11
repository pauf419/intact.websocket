package com.intact.wss.controller;

import com.intact.wss.model.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@CrossOrigin(origins="http://localhost:3000")
public class ChatController {

    @Autowired private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/message")
    public ChatMessage sendMessage(
            @Payload  ChatMessage chatMessage
    ) {
        String tid = chatMessage.getTopicId();

        messagingTemplate.convertAndSend("/topic/" + tid + "/flow", chatMessage);

        return chatMessage;
    }

    @MessageMapping("/chat/user")
    public ChatMessage addUser(
            @Payload ChatMessage chatMessage,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        String tid = chatMessage.getTopicId();

        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        headerAccessor.getSessionAttributes().put("topicId", tid);

        messagingTemplate.convertAndSend("/topic/" + tid + "/flow", chatMessage);

        return chatMessage;
    }
}
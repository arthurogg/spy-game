package com.projet.spy_game.controller;

import com.projet.spy_game.dto.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat.sendMessage")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        System.out.println(">>> MESSAGE RECEIVED: " + chatMessage.getContent());
        messagingTemplate.convertAndSend("/topic/game/" + chatMessage.getGameCode(), chatMessage);
        return chatMessage;
    }
}
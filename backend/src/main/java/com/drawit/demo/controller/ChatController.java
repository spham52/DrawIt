package com.drawit.demo.controller;

import com.drawit.demo.websocket.ChatMessage;
import com.drawit.demo.websocket.ChatMessageResponse;
import com.drawit.demo.websocket.UserMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @MessageMapping("/chat/message")
    @SendTo("/topic/chat")
    public ChatMessageResponse connect(ChatMessage chatMessage) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        return new ChatMessageResponse (
                chatMessage.getPlayerID(),
                chatMessage.getGameID(),
                chatMessage.getMessage(),
                timestamp
        );
    }
}

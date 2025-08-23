package com.drawit.demo.service;

import com.drawit.demo.model.Game;
import com.drawit.demo.websocket.ChatMessage;
import com.drawit.demo.websocket.ChatMessageResponse;
import org.springframework.stereotype.Service;


public interface ChatService {

    ChatMessageResponse constructChatMessageResponse(String websocketID, ChatMessage chatMessage);
}

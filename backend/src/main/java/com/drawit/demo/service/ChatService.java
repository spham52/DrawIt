package com.drawit.demo.service;

import com.drawit.demo.websocket.ChatMessage;

public interface ChatService {

    void sendMessage(ChatMessage message);
}

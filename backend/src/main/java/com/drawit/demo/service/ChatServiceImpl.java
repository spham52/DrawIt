package com.drawit.demo.service;

import com.drawit.demo.model.Game;
import com.drawit.demo.model.Player;
import com.drawit.demo.model.Session;
import com.drawit.demo.websocket.ChatMessage;
import com.drawit.demo.websocket.ChatMessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ChatServiceImpl implements ChatService {

    private final SessionService sessionService;
    private final GameService gameService;

    @Autowired
    public ChatServiceImpl(SessionService sessionService, GameService gameService) {
        this.sessionService = sessionService;
        this.gameService = gameService;
    }

    @Override
    public ChatMessageResponse constructChatMessageResponse(String websocketID, ChatMessage chatMessage) {
        Session session = sessionService.getSession(websocketID);
        UUID playerID = session.getPlayerID();
        UUID gameID = session.getGameID();

        Game game = gameService.getGame(gameID);
        Player player = game.getPlayers().get(playerID);

        String timestamp = java.time.Instant.now().toString();

        ChatMessageResponse chatMessageResponse = new ChatMessageResponse();
        chatMessageResponse.setMessage(chatMessage.getMessage());
        chatMessageResponse.setTimestamp(timestamp);
        chatMessageResponse.setUsername(player.getUsername());

        return chatMessageResponse;
    }
}

package com.drawit.demo.controller;

import com.drawit.demo.model.Game;
import com.drawit.demo.model.Player;
import com.drawit.demo.service.ChatService;
import com.drawit.demo.service.GameService;
import com.drawit.demo.service.PlayerService;
import com.drawit.demo.websocket.ChatMessage;
import com.drawit.demo.websocket.ChatMessageResponse;
import com.drawit.demo.websocket.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
public class ChatController {
    private final GameService gameService;
    private final PlayerService playerService;

    public ChatController(GameService gameService, PlayerService playerService) {
        this.gameService = gameService;
        this.playerService = playerService;
    }

    // construct response message to front-end, so it can display username by mapping playerID
    @MessageMapping("/chat/message")
    @SendTo("/topic/chat")
    public ChatMessageResponse connect(ChatMessage chatMessage) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        UUID gameUUID = UUID.fromString(chatMessage.getGameID());

        Game game = gameService.getGame(gameUUID);
        Player player = playerService.findPlayer(chatMessage.getPlayerID(), game);

        return new ChatMessageResponse (
                player.getUsername(),
                game.getGameID().toString(),
                chatMessage.getMessage(),
                timestamp
        );
    }
}

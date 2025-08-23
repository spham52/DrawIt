package com.drawit.demo.controller;

import com.drawit.demo.model.Game;
import com.drawit.demo.model.Player;
import com.drawit.demo.model.Session;
import com.drawit.demo.service.*;
import com.drawit.demo.util.GameRules;
import com.drawit.demo.websocket.ChatMessage;
import com.drawit.demo.websocket.ChatMessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
public class ChatController {
    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;
    private final GameRules gameRules = new GameRules();
    private final GameService gameService;
    private final GameMessagingService gameMessagingService;
    private final GameLogicService gameLogicService;
    private final SessionService sessionService;

    @Autowired
    public ChatController(ChatService chatService, SimpMessagingTemplate messagingTemplate,
                          GameService gameService, GameMessagingService gameMessagingService,
                          GameLogicService gameLogicService, SessionService sessionService) {
        this.chatService = chatService;
        this.messagingTemplate = messagingTemplate;
        this.gameService = gameService;
        this.gameMessagingService = gameMessagingService;
        this.gameLogicService = gameLogicService;
        this.sessionService = sessionService;
    }

    // user subscribes to app/topic/game/{GAMEID},
    // then it will start sending ChatMessage to /topic/messages
    // function sends back ChatMessageResponse to /topic/{gameID}
    @MessageMapping("/chat")
    public void sendMessage(ChatMessage chatMessage, SimpMessageHeaderAccessor accessor) {
        // get session from connection ID
        String socketID = accessor.getSessionId();
        Session session = sessionService.getSession(socketID);

        UUID gameID = session.getGameID();

        Game game = gameService.getGame(gameID);
        Player player = session.getPlayer();

        // game hasn't started or player isn't allowed to message
        if (!gameRules.hasGameStarted(game) || !gameRules.canPlayerSendMessage(player, game)) return;

        // if message is correct guess
        if (gameRules.isCorrectGuess(chatMessage, game)) {
            gameLogicService.handleCorrectGuess(player, game);
            gameMessagingService.sendCorrectGuessAnnouncement(player, game);
            gameMessagingService.sendCurrentWord(player, game);
            return;
        }

        ChatMessageResponse response = chatService.constructChatMessageResponse(chatMessage);
        messagingTemplate.convertAndSend("/topic/game/" + chatMessage.getGameID(), response);
    }
}

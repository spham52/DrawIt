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
    private GameSchedulerService gameSchedulerService;
    private final GameService gameService;
    private final GameMessagingService gameMessagingService;
    private final GameLogicService gameLogicService;
    private final SessionService sessionService;

    @Autowired
    public ChatController(ChatService chatService, SimpMessagingTemplate messagingTemplate,
                          GameService gameService, GameMessagingService gameMessagingService,
                          GameLogicService gameLogicService, SessionService sessionService,
                          GameSchedulerService gameSchedulerService) {
        this.chatService = chatService;
        this.messagingTemplate = messagingTemplate;
        this.gameService = gameService;
        this.gameMessagingService = gameMessagingService;
        this.gameLogicService = gameLogicService;
        this.sessionService = sessionService;
        this.gameSchedulerService = gameSchedulerService;
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
        UUID playerID = session.getPlayerID();

        Game game = gameService.getGame(gameID);
        Player player = game.getPlayers().get(playerID);

        if (!GameRules.canPlayerSendMessage(player, game)) return;

        if (GameRules.isCorrectGuess(chatMessage, game)) {
            gameLogicService.handleCorrectGuess(player, game);
            gameMessagingService.sendCorrectGuessAnnouncement(player, game);

            if (GameRules.isCorrectGuess(chatMessage, game)) {
                gameLogicService.handleCorrectGuess(player, game);
                gameMessagingService.sendCorrectGuessAnnouncement(player, game);

                if (GameRules.allPlayersGuessedCorrectly(game)) {
                    gameSchedulerService.advanceTurnNow(game);
                }
                return;
            }

            return;
        }

        ChatMessageResponse response = chatService.constructChatMessageResponse(socketID, chatMessage);
        messagingTemplate.convertAndSend("/topic/game/" + chatMessage.getGameID(), response);
    }
}

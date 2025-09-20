package com.drawit.demo.controller;

import com.drawit.demo.dto.Coordinates;
import com.drawit.demo.model.Game;
import com.drawit.demo.model.Player;
import com.drawit.demo.model.Session;
import com.drawit.demo.service.*;
import com.drawit.demo.util.GameRules;
import com.drawit.demo.websocket.ServerMessage;
import com.drawit.demo.websocket.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Controller
public class SessionController {
    private final SessionService sessionService;
    private final PlayerService playerServiceImpl;
    private final GameSchedulerService gameSchedulerService;
    private final GameMessagingService gameMessagingService;
    private final GameService gameService;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public SessionController(SessionService sessionService, PlayerService playerServiceImpl,
                             GameSchedulerService gameSchedulerService,
                             GameMessagingService gameMessagingService,
                             GameService gameService, SimpMessagingTemplate messagingTemplate) {
        this.sessionService = sessionService;
        this.playerServiceImpl = playerServiceImpl;
        this.gameSchedulerService = gameSchedulerService;
        this.gameMessagingService = gameMessagingService;
        this.gameService = gameService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/connect")
    public void connect(UserMessage userMessage, SimpMessageHeaderAccessor accessor) {
        String sessionId = accessor.getSessionId();

        Player player = playerServiceImpl.createPlayer(userMessage);
        Session session = sessionService.createSession(player, sessionId);

        Game game = gameService.getGame(session.getGameID());

        System.out.println(game);

        // send session details privately
        ServerMessage sessionDto = ServerMessage.from(session, player, game);

        if (GameRules.canGameStart(game)) {
            gameSchedulerService.createTask(game.getGameID());
        }

        messagingTemplate.convertAndSendToUser(
                sessionId,
                "/queue/session",
                sessionDto,
                accessor.getMessageHeaders()
        );
    }

    @EventListener
    public void onDisconnectEvent(SessionDisconnectEvent event) {
        Session session = sessionService.getSession(event.getSessionId());
        Game game = gameService.getGame(session.getGameID());
        Player player = game.getPlayers().get(session.getPlayerID());
        sessionService.removeSession(event.getSessionId());
        gameMessagingService.sendPlayerLeft(player, game);
    }

}

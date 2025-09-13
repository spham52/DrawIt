package com.drawit.demo.controller;

import com.drawit.demo.model.Game;
import com.drawit.demo.model.Player;
import com.drawit.demo.model.Session;
import com.drawit.demo.service.*;
import com.drawit.demo.websocket.ServerMessage;
import com.drawit.demo.websocket.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Controller
public class SessionController {
    private final SessionService sessionService;
    private final PlayerService playerServiceImpl;
    private final GameSchedulerService gameSchedulerService;
    private final GameMessagingService gameMessagingService;
    private final GameService gameService;

    @Autowired
    public SessionController(SessionService sessionService, PlayerService playerServiceImpl,
                             GameSchedulerService gameSchedulerService, GameMessagingService gameMessagingService, GameService gameService) {
        this.sessionService = sessionService;
        this.playerServiceImpl = playerServiceImpl;
        this.gameSchedulerService = gameSchedulerService;
        this.gameMessagingService = gameMessagingService;
        this.gameService = gameService;
    }

    // function that creates session when user subscribes to '/connect'
    // sends session details to '/topic/session'
    @MessageMapping("/connect")
    @SendTo("/topic/session")
    public ServerMessage connect(UserMessage userMessage, SimpMessageHeaderAccessor accessor) {
        // get websocket session ID
        String socketID = accessor.getSessionId();

        Player player = playerServiceImpl.createPlayer(userMessage);
        Session session = sessionService.createSession(player, socketID);
        gameSchedulerService.createTask(session.getGameID());
        Game game = gameService.getGame(session.getGameID());
        gameMessagingService.sendPlayerJoined(player, game);

        if (game.isGameStarted()) {
            gameMessagingService.sendCurrentDrawer(player, game);
        }

        return new ServerMessage(
                session.getSessionID().toString(),
                session.getGameID().toString(),
                player.getId().toString()
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

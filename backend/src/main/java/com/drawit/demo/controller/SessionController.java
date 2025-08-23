package com.drawit.demo.controller;

import com.drawit.demo.model.Game;
import com.drawit.demo.model.Player;
import com.drawit.demo.model.Session;
import com.drawit.demo.service.*;
import com.drawit.demo.websocket.ServerMessage;
import com.drawit.demo.websocket.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class SessionController {
    private final SessionService sessionService;
    private final PlayerService playerServiceImpl;
    private final GameSchedulerService gameSchedulerService;

    @Autowired
    public SessionController(SessionService sessionService, PlayerService playerServiceImpl,
                             GameSchedulerService gameSchedulerService) {
        this.sessionService = sessionService;
        this.playerServiceImpl = playerServiceImpl;
        this.gameSchedulerService = gameSchedulerService;
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
        return new ServerMessage(
                session.getSessionID().toString(),
                session.getGameID().toString(),
                player.getId().toString()
        );
    }

}

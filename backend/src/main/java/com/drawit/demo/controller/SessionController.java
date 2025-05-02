package com.drawit.demo.controller;

import com.drawit.demo.model.Player;
import com.drawit.demo.model.Session;
import com.drawit.demo.service.PlayerService;
import com.drawit.demo.service.SessionService;
import com.drawit.demo.websocket.ServerMessage;
import com.drawit.demo.websocket.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
public class SessionController {
    private final SessionService sessionService;
    private final PlayerService playerService;

    @Autowired
    public SessionController(SessionService sessionService, PlayerService playerService) {
        this.sessionService = sessionService;
        this.playerService = playerService;
    }

    @MessageMapping("/connect")
    @SendTo("/topic/session")
    public ServerMessage connect(UserMessage userMessage) {
        Player player = playerService.createPlayer(userMessage);
        Session session = sessionService.createSession(player);
        return new ServerMessage(
                session.getSessionID().toString(),
                session.getGameID().toString(),
                player.getId().toString()
        );
    }

}

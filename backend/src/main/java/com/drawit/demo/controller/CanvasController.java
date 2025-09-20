package com.drawit.demo.controller;

import com.drawit.demo.dto.Coordinates;
import com.drawit.demo.model.Game;
import com.drawit.demo.model.Player;
import com.drawit.demo.model.Session;
import com.drawit.demo.service.GameService;
import com.drawit.demo.service.SessionService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@Controller
public class CanvasController {

    private final SessionService sessionService;
    private final SimpMessagingTemplate messagingTemplate;
    private final GameService gameService;

    public CanvasController(SessionService sessionService, SimpMessagingTemplate messagingTemplate, GameService gameService) {
        this.sessionService = sessionService;
        this.messagingTemplate = messagingTemplate;
        this.gameService = gameService;
    }

    @MessageMapping("/game/{id}/draw")
    public void sendCanvas(@DestinationVariable String id,
                           Coordinates coords,
                           SimpMessageHeaderAccessor accessor) {
        String websocketID = accessor.getSessionId();
        Session session = sessionService.getSession(websocketID);

        UUID playerID = session.getPlayerID();

        Game game = gameService.getGame(session.getGameID());
        Player player = game.getPlayers().get(playerID);

        if (game.getDrawer().getId() == player.getId()) {
            UUID gameID = game.getGameID();
            String url = "/topic/game/" + gameID + "/draw";
            game.getDrawHistory().add(coords);
            messagingTemplate.convertAndSend(url, coords);
        }
    }

    @MessageMapping("/game/{gameID}/requestHistory")
    public void sendHistory(@DestinationVariable String gameID,
                            SimpMessageHeaderAccessor accessor) {
        UUID gid = UUID.fromString(gameID);
        Game game = gameService.getGame(gid);
        if (game == null) return;

        messagingTemplate.convertAndSendToUser(
                accessor.getSessionId(),
                "/queue/draw",
                game.getDrawHistory(),
                accessor.getMessageHeaders()
        );
    }
}

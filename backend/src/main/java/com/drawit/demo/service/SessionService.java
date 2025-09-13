package com.drawit.demo.service;

import com.drawit.demo.model.Game;
import com.drawit.demo.model.Player;
import com.drawit.demo.model.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionService {
    private final GameService gameService;
    private final GameMessagingService messagingService;

    // maps websocket connection ID to session
    private final ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<>();
    private final GameLogicService gameLogicService;
    private final GameSchedulerService gameSchedulerService;

    @Autowired
    SessionService(GameService gameService, GameMessagingService messagingService, GameLogicService gameLogicService, GameSchedulerService gameSchedulerService) {
        this.gameService = gameService;
        this.messagingService = messagingService;
        this.gameLogicService = gameLogicService;
        this.gameSchedulerService = gameSchedulerService;
    }

    public Session createSession(Player player, String socketID) {
        UUID uuid = UUID.randomUUID();
        UUID gameID = UUID.fromString("00000000-0000-0000-0000-000000000001");

        if (gameService.getGame(gameID) == null) {
            Game game = new Game();
            game.setGameID(gameID);
            game.setMaxRounds(3);
            gameService.addGame(game);
        }

        gameService.addPlayer(player, gameID);
        Game game = gameService.getGame(gameID);

        // if game has started and player has joined, give player the drawer's ID
        if (game.isGameStarted()) {
            messagingService.sendPlayerJoined(game.getDrawer(), game);
        }

        Session session = new Session();
        session.setSessionID(uuid);
        session.setPlayerID(player.getId());
        session.setGameID(gameID);

        sessions.put(socketID, session);
        return session;
    }

    public Session getSession(String socketID) {
        return sessions.get(socketID);
    }

    public void removeSession(String socketID) {
        Session session = getSession(socketID);
        sessions.remove(socketID);
        Game game = gameService.getGame(session.getGameID());
        Player player = game.getPlayers().get(session.getPlayerID());
        gameService.removePlayer(player, game);

        switch(gameLogicService.handlePlayerLeft(player, game)) {
            case NO_OP:
                break;
            case ADVANCE_TURN:
                gameSchedulerService.rescheduleTask(game);
                break;
            case STOP_SCHEDULER:
                gameSchedulerService.stopGame(game);
                break;
        }
    }
}

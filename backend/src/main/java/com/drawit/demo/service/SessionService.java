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

    // maps websocket connection ID to session
    private ConcurrentHashMap<String, Session> sessions;

    @Autowired
    SessionService(GameService gameService) {
        this.gameService = gameService;
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
        sessions.remove(socketID);
    }
}

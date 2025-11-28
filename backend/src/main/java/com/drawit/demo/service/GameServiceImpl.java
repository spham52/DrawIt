package com.drawit.demo.service;

import com.drawit.demo.model.Game;
import com.drawit.demo.model.Player;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameServiceImpl implements GameService {
    private final Map<UUID, Game> games = new ConcurrentHashMap<>();

    public GameServiceImpl() {
    }

    @Override
    public Game getGame(UUID gameID) {
        return games.get(gameID);
    }

    @Override
    public void addGame(Game game) {
        games.put(game.getGameID(), game);
    }

    @Override
    public void addPlayer(Player player, UUID gameID) {
        Game game = getGame(gameID);
        game.getPlayers().put(player.getId(), player);
    }

    @Override
    public void stopGame(UUID gameID) {

    }

    @Override
    public Game restartGame(Game game) {
        game.getPlayerTurns().clear();

        for (Player p : game.getPlayers().values()) {
            game.getPlayerTurns().add(p);
        }
        game.setCurrRound(0);
        game.setGameStarted(false);
        game.setDrawer(null);
        game.getGuessedCorrectly().clear();

        return game;
    }

    @Override
    public void removePlayer(Player player, Game game) {
        game.getPlayers().remove(player.getId());
        game.getPlayerTurns().remove(player);
    }


}

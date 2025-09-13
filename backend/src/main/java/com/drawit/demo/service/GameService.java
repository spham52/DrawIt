package com.drawit.demo.service;

import com.drawit.demo.model.Game;
import com.drawit.demo.model.Player;

import java.util.UUID;

public interface GameService {

    Game getGame(UUID gameID);

    void addGame(Game game);

    void addPlayer(Player player, UUID gameID);

    void stopGame(UUID gameID);

    Game restartGame(Game game);

    void removePlayer(Player player, Game game);
}

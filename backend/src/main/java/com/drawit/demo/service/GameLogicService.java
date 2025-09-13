package com.drawit.demo.service;

import com.drawit.demo.model.Game;
import com.drawit.demo.model.Player;
import com.drawit.demo.model.enums.PlayerLeftOutcome;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public interface GameLogicService {
    Player getNextPlayer(Game game);
    void nextPlayer(Game game);
    void handleCorrectGuess(Player player, Game game);
    void incrementRound(Game game);
    boolean isGameFinished(Game game);
    PlayerLeftOutcome handlePlayerLeft(Player player, Game game);
}

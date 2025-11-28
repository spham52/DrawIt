package com.drawit.demo.service;

import com.drawit.demo.dto.WordEntry;
import com.drawit.demo.model.Game;
import com.drawit.demo.model.Player;
import com.drawit.demo.model.enums.PlayerLeftOutcome;
import com.drawit.demo.util.GameRules;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

// gameService will retrieve the game
// GameLogic will do whatever and then gameService will update the changes
@Service
public class GameLogicServiceImpl implements GameLogicService {

    private final WordService wordService;
    private final GameMessagingService gameMessagingService;

    @Autowired
    public GameLogicServiceImpl(WordService wordService,
                                GameMessagingService gameMessagingService) {
        this.wordService = wordService;
        this.gameMessagingService = gameMessagingService;
    }

    public void incrementRound(Game game) {
        if (game.getCurrRound() == 0) {
            game.getPlayerTurns().clear();
            for (Player p : game.getPlayers().values()) {
                game.getPlayerTurns().add(p);
            }
            game.setCurrRound(1);
            gameMessagingService.sendRoundStart(game);
            return;
        }

        if (GameRules.isRoundOver(game)) {
            Map<UUID, Player> players = game.getPlayers();
            for (Player p : players.values()) {
                game.getPlayerTurns().add(p);
            }
            game.setCurrRound(game.getCurrRound() + 1);
            gameMessagingService.sendRoundStart(game);
        }
    }

    public boolean isGameFinished(Game game) {
        if (GameRules.isGameOver(game.getCurrRound())) {
            gameMessagingService.sendGameOver(game);
            return true;
        }
        return false;
    }

    public Player getNextPlayer(Game game) {
        Queue<Player> players = game.getPlayerTurns();

        if (players.isEmpty()) {
            System.out.println("No players in game");
            return null;
        }
        return players.poll();
    }

    public void nextPlayer(Game game) {
        Player player = getNextPlayer(game);

        // queue is unexpectedly empty, rebuild queue from current players again
        // otherwise if still empty, skip
        if (player == null) {
            if (game.getPlayers().isEmpty()) return;

            for (Player p : game.getPlayers().values()) {
                game.getPlayerTurns().add(p);
            }
            player = getNextPlayer(game);
            if (player == null) return;
        }

        WordEntry word = wordService.retrieveRandomWord();
        game.setDrawer(player);
        game.getGuessedCorrectly().clear();
        game.getDrawHistory().clear();


        // send word to current drawer
        game.setCurrWord(word.getWord());
        System.out.println(word.getWord());
        gameMessagingService.sendCurrentWord(player, game);
        gameMessagingService.sendCurrentDrawer(game);
    }

    public void handleCorrectGuess(Player player, Game game) {
        game.getGuessedCorrectly().put(player.getId(), player);
    }

    public PlayerLeftOutcome handlePlayerLeft(Player player, Game game) {
        boolean wasDrawer = game.getDrawer() != null && game.getDrawer().getId().equals(player.getId());

        if (game.getPlayers().size() < 2) {
            return PlayerLeftOutcome.STOP_SCHEDULER;
        }

        if (wasDrawer) {
            nextPlayer(game);
            return PlayerLeftOutcome.ADVANCE_TURN;
        }

        return PlayerLeftOutcome.NO_OP;
    }
}

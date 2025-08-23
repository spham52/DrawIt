package com.drawit.demo.service;

import com.drawit.demo.dto.WordEntry;
import com.drawit.demo.model.Game;
import com.drawit.demo.model.Player;
import com.drawit.demo.util.GameRules;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

// gameService will retrieve the game
// GameLogic will do whatever and then gameService will update the changes
@Service
public class GameLogicServiceImpl implements GameLogicService {

    private final WordService wordService;
    private final GameMessagingService gameMessagingService;
    private final GameRules gameRules = new GameRules();

    @Autowired
    public GameLogicServiceImpl(WordService wordService,
                                GameMessagingService gameMessagingService) {
        this.wordService = wordService;
        this.gameMessagingService = gameMessagingService;
    }

    public void incrementRound(Game game) {
        if (gameRules.isRoundOver(game.getCounter(), game.getPlayers().size())) {
            game.setCounter(0);
            game.setCurrRound(game.getCurrRound() + 1);
            gameMessagingService.sendRoundOver(game);
        }
    }

    public boolean isGameFinished(Game game) {
        if (gameRules.isGameOver(game.getCurrRound())) {
            gameMessagingService.sendGameOver(game);
            return true;
        }
        return false;
    }

    public Player getNextPlayer(int i, Game game) {
        List<Player> players = new ArrayList<>(game.getPlayers().values());

        if (players.isEmpty()) {
            System.out.println("No players in game");
            return null;
        }
        int safeIndex = i % players.size();
        return players.get(safeIndex);
    }

    public void nextPlayer(int i, Game game) {
        Player player = getNextPlayer(i, game);
        WordEntry word = wordService.retrieveRandomWord();
        game.setDrawer(player);
        game.getGuessedCorrectly().clear();

        // send word to current drawer
        game.setCurrWord(word.getWord());
        System.out.println(word.getWord());
        gameMessagingService.sendCurrentWord(player, game);

    }

    public void handleCorrectGuess(Player player, Game game) {
        gameMessagingService.sendCorrectGuessAnnouncement(player, game);
        game.getGuessedCorrectly().put(player.getId(), player);
    }
}

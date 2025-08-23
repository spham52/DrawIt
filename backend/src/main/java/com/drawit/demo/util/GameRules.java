package com.drawit.demo.util;

import com.drawit.demo.model.Game;
import com.drawit.demo.model.Player;
import com.drawit.demo.websocket.ChatMessage;

import java.util.concurrent.atomic.AtomicInteger;

public class GameRules {

    public boolean isRoundOver(int count, int playerSize) {
        return count > playerSize - 1;
    }

    public boolean isGameOver(int currRound) {
        return currRound > 3;
    }

    public boolean isCorrectGuess(ChatMessage chatMessage, Game game) {
        return chatMessage.getMessage().equalsIgnoreCase(game.getCurrWord());
    }

    public boolean canPlayerSendMessage(Player player, Game game) {
//        return !game.getDrawer().getId = && !game.getGuessedCorrectly().containsKey(player.getId());
        return game.getDrawer().getId() != player.getId() && !game.getGuessedCorrectly().containsKey(player.getId());
    }

    public boolean hasGameStarted(Game game) {
        return game.isGameStarted();
    }

    public boolean canGameStart(Game game) {
        return game.getPlayers().size() > 1;
    }

    public boolean allPlayersGuessed(Game game) {
        return game.getGuessedCorrectly().size() == game.getPlayers().size() - 1;
    }
}

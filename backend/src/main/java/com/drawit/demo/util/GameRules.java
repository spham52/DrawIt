package com.drawit.demo.util;

import com.drawit.demo.model.Game;
import com.drawit.demo.model.Player;
import com.drawit.demo.websocket.ChatMessage;

public class GameRules {

    public static boolean isRoundOver(Game game) {
        return game.getPlayerTurns().isEmpty();
    }

    public static boolean isGameOver(int currRound) {
        return currRound > 3;
    }

    public static boolean isCorrectGuess(ChatMessage chatMessage, Game game) {
        return chatMessage.getMessage().equalsIgnoreCase(game.getCurrWord());
    }

    public static boolean canPlayerSendMessage(Player player, Game game) {
        if (!game.isGameStarted()) return true;

        if (game.getDrawer() == null) return true;

        return game.getDrawer().getId() != player.getId() && !game.getGuessedCorrectly().containsKey(player.getId());
    }

    public static boolean hasGameStarted(Game game) {
        return game.isGameStarted();
    }

    public static boolean canGameStart(Game game) {
        return game.getPlayers().size() > 1;
    }

    public static boolean allPlayersGuessedCorrectly(Game game) {
        return game.getGuessedCorrectly().size() == game.getPlayers().size() - 1;
    }
}

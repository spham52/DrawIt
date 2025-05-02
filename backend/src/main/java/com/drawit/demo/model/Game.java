package com.drawit.demo.model;

import com.drawit.demo.websocket.ChatMessage;

import java.util.List;
import java.util.UUID;

public class Game {

    UUID gameID;
    List<Player> players;
    int maxRounds;
    boolean gameOver = false;
    List<ChatMessage> chatHistory;

    public Game(UUID gameID, List<Player> players, int maxRounds, List<ChatMessage> chatHistory) {
        this.gameID = gameID;
        this.players = players;
        this.maxRounds = maxRounds;
        this.chatHistory = chatHistory;
    }

    public Game() {}

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public int getMaxRounds() {
        return maxRounds;
    }

    public void setMaxRounds(int maxRounds) {
        this.maxRounds = maxRounds;
    }

    public UUID getGameID() {
        return gameID;
    }

    public void setGameID(UUID gameID) {
        this.gameID = gameID;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public List<ChatMessage> getChatHistory() {
        return chatHistory;
    }

    public void setChatHistory(List<ChatMessage> chatHistory) {
        this.chatHistory = chatHistory;
    }
}

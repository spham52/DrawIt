package com.drawit.demo.model;

import com.drawit.demo.websocket.ChatMessage;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Game {

    UUID gameID;
    private Map<UUID, Player> players = new ConcurrentHashMap<>();
    int maxRounds;
    boolean gameOver = false;

    public Game(UUID gameID, Map<UUID, Player> players, int maxRounds, boolean gameOver) {
        this.gameID = gameID;
        this.players = players;
        this.maxRounds = maxRounds;
        this.gameOver = gameOver;
    }

    public Game() {}

    public int getMaxRounds() {
        return maxRounds;
    }

    public void setMaxRounds(int maxRounds) {
        this.maxRounds = maxRounds;
    }

    public Map<UUID, Player> getPlayers() {
        return players;
    }

    public void setPlayers(Map<UUID, Player> players) {
        this.players = players;
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

    @Override
    public String toString() {
        return "Game{" +
                "gameID=" + gameID +
                ", players=" + players +
                ", maxRounds=" + maxRounds +
                ", gameOver=" + gameOver +
                '}';
    }
}

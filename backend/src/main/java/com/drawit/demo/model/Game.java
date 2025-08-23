package com.drawit.demo.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Game {

    private UUID gameID;
    private int maxRounds = 3;
    private String currWord;
    private Player drawer;
    private boolean gameStarted = false;
    private int counter = 0;
    private int currRound = 0;
    private Map<UUID, Player> players = new ConcurrentHashMap<>();
    private Map<UUID, Player> guessedCorrectly = new ConcurrentHashMap<>();


    public Game(UUID gameID, Map<UUID, Player> players, Map<UUID, Player> guessedCorrectly, int maxRounds,
                String currWord, Player drawer, int currRound) {
        this.gameID = gameID;
        this.players = players;
        this.guessedCorrectly = guessedCorrectly;
        this.maxRounds = maxRounds;
        this.currWord = currWord;
        this.drawer = drawer;
        this.currRound = currRound;
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

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public int getCurrRound() {
        return currRound;
    }

    public void setCurrRound(int currRound) {
        this.currRound = currRound;
    }

    public Map<UUID, Player> getGuessedCorrectly() {
        return guessedCorrectly;
    }

    public void setGuessedCorrectly(Map<UUID, Player> guessedCorrectly) {
        this.guessedCorrectly = guessedCorrectly;
    }

    public Player getDrawer() {
        return drawer;
    }

    public void setDrawer(Player drawer) {
        this.drawer = drawer;
    }

    public UUID getGameID() {
        return gameID;
    }

    public void setGameID(UUID gameID) {
        this.gameID = gameID;
    }

    public String getCurrWord() {
        return currWord;
    }

    public void setCurrWord(String currWord) {
        this.currWord = currWord;
    }

    @Override
    public String toString() {
        return "Game{" +
                "gameID=" + gameID +
                ", players=" + players +
                ", guessedCorrectly=" + guessedCorrectly +
                ", maxRounds=" + maxRounds +
                ", currWord='" + currWord + '\'' +
                ", drawer=" + drawer +
                ", currRound=" + currRound +
                '}';
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }
}

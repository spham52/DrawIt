package com.drawit.demo.model;

import java.util.List;
import java.util.UUID;

public class Session {

    UUID sessionID;
    Player player;
    UUID gameID;
    boolean isConnected;

    public Session(UUID sessionID, Player player, UUID gameID, boolean isConnected) {
        this.sessionID = sessionID;
        this.player = player;
        this.gameID = gameID;
        this.isConnected = isConnected;
    }

    public Session() {
    }

    public UUID getSessionID() {
        return sessionID;
    }

    public void setSessionID(UUID sessionID) {
        this.sessionID = sessionID;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public UUID getGameID() {
        return gameID;
    }

    public void setGameID(UUID gameID) {
        this.gameID = gameID;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }
}

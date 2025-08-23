package com.drawit.demo.model;

import java.util.UUID;

public class Session {

    UUID sessionID;
    UUID playerID;
    UUID gameID;
    boolean isConnected;

    public Session(UUID sessionID, UUID playerID, UUID gameID, boolean isConnected) {
        this.sessionID = sessionID;
        this.playerID = playerID;
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

    public UUID getPlayerID() {
        return playerID;
    }

    public void setPlayerID(UUID playerID) {
        this.playerID = playerID;
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

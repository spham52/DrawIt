package com.drawit.demo.websocket;

public class ServerMessage {
    String sessionID;
    String gameID;
    String playerID;

    public ServerMessage() {
    }

    public ServerMessage(String sessionID, String gameID, String playerID) {
        this.sessionID = sessionID;
        this.gameID = gameID;
        this.playerID = playerID;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getGameID() {
        return gameID;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public String getPlayerID() {
        return playerID;
    }

    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }
}

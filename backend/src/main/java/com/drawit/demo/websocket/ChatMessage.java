package com.drawit.demo.websocket;

import java.util.UUID;

public class ChatMessage {

    String playerID;
    String gameID;
    String message;

    public ChatMessage() {
    }

    public ChatMessage(String playerID, String gameID, String message) {
        this.playerID = playerID;
        this.gameID = gameID;
        this.message = message;
    }

    public String getPlayerID() {
        return playerID;
    }

    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getGameID() {
        return gameID;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }
}

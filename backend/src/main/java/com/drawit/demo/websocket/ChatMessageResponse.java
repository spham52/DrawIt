package com.drawit.demo.websocket;

public class ChatMessageResponse {
    String playerID;
    String gameID;
    String message;
    String timestamp;

    public ChatMessageResponse(String playerID, String gameID, String message, String timestamp) {
        this.playerID = playerID;
        this.gameID = gameID;
        this.message = message;
        this.timestamp = timestamp;
    }

    public ChatMessageResponse() {
    }

    public String getPlayerID() {
        return playerID;
    }

    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }

    public String getGameID() {
        return gameID;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

package com.drawit.demo.websocket;

public class ChatMessageResponse {
    String username;
    String gameID;
    String message;
    String timestamp;

    public ChatMessageResponse(String username, String gameID, String message, String timestamp) {
        this.username = username;
        this.gameID = gameID;
        this.message = message;
        this.timestamp = timestamp;
    }

    public ChatMessageResponse() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

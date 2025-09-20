package com.drawit.demo.websocket;

import com.drawit.demo.model.Game;
import com.drawit.demo.model.Player;
import com.drawit.demo.model.Session;

public class ServerMessage {
    String sessionID;
    String gameID;
    String playerID;
    String drawerID;
    String drawerName;

    public ServerMessage() {
    }

    public ServerMessage(String sessionID, String gameID, String playerID,  String drawerID, String drawerName) {
        this.sessionID = sessionID;
        this.gameID = gameID;
        this.playerID = playerID;
        this.drawerID = drawerID;
        this.drawerName = drawerName;
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

    public String getDrawerID() {
        return drawerID;
    }

    public void setDrawerID(String drawerID) {
        this.drawerID = drawerID;
    }

    public String getDrawerName() {
        return drawerName;
    }

    public void setDrawerName(String drawerName) {
        this.drawerName = drawerName;
    }

    public static ServerMessage from(Session session, Player player, Game game) {
        String drawerID = null;
        String drawerName = null;

        if (game.isGameStarted() && game.getDrawer() != null) {
            drawerID = game.getDrawer().getId().toString();
            drawerName = game.getDrawer().getUsername();
        }

        return new ServerMessage(
                session.getSessionID().toString(),
                session.getGameID().toString(),
                player.getId().toString(),
                drawerID,
                drawerName
        );
    }
}

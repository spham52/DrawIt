package com.drawit.demo.websocket;

import com.drawit.demo.model.enums.EventMessageType;

import java.util.UUID;

public class GameEventMessage {

    EventMessageType eventID;
    String message;
    UUID drawerID;

    public GameEventMessage(EventMessageType eventID, String message) {
        this.eventID = eventID;
        this.message = message;
    }

    public GameEventMessage(EventMessageType eventID, String message, UUID drawerID) {
        this.eventID = eventID;
        this.message = message;
        this.drawerID = drawerID;
    }

    public GameEventMessage(EventMessageType eventID, UUID drawerID) {
        this.eventID = eventID;
        this.drawerID = drawerID;
    }

    public GameEventMessage(EventMessageType eventID) {
        this.eventID = eventID;
    }

    public EventMessageType getEventID() {
        return eventID;
    }

    public void setEventID(EventMessageType eventID) {
        this.eventID = eventID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UUID getDrawerID() {
        return drawerID;
    }

    public void setDrawerID(UUID drawerID) {
        this.drawerID = drawerID;
    }

    @Override
    public String toString() {
        return "GameEventMessage{" +
                "eventID=" + eventID +
                ", message='" + message + '\'' +
                '}';
    }
}

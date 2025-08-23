package com.drawit.demo.websocket;

public class UserMessage {

    private String username;
    private String inviteCode;

    public UserMessage(String username, String inviteCode) {
        this.username = username;
        this.inviteCode = inviteCode;
    }

    public UserMessage() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }
}

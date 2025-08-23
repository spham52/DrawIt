package com.drawit.demo.service;

import com.drawit.demo.model.Game;
import com.drawit.demo.model.Player;
import com.drawit.demo.websocket.UserMessage;

import java.util.UUID;

public interface PlayerService {
    Player createPlayer(UserMessage userMessage);
}

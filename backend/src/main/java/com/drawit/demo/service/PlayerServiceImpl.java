package com.drawit.demo.service;

import com.drawit.demo.model.Game;
import com.drawit.demo.model.Player;
import com.drawit.demo.websocket.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.UUID;

@Service
public class PlayerServiceImpl implements PlayerService {

    public PlayerServiceImpl() {
    }

    public Player createPlayer(UserMessage userMessage) {
        Player player = new Player();
        UUID uuid = UUID.randomUUID();
        player.setId(uuid);
        player.setUsername(userMessage.getUsername());
        return player;
    }
}

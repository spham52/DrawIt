package com.drawit.demo.service;

import com.drawit.demo.model.Player;
import com.drawit.demo.model.Session;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SessionService {

    public Session createSession(Player player) {
        UUID uuid = UUID.randomUUID();
        UUID gameID = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Session session = new Session();
        session.setSessionID(uuid);
        session.setPlayer(player);
        session.setGameID(gameID);
        return session;
    }
}

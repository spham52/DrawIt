package com.drawit.demo.service;

import com.drawit.demo.model.Game;
import com.drawit.demo.model.Player;

// used for websocket communication
public interface GameMessagingService {

    void sendCurrentWord(Player player, Game game);

    void sendRoundStart(Game game);

    void sendRoundOver(Game game);

    void sendCorrectGuessAnnouncement(Player player, Game game);

    void sendGameOver(Game game);
}

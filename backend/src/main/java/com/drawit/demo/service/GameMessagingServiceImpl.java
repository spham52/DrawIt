package com.drawit.demo.service;

import com.drawit.demo.model.Game;
import com.drawit.demo.model.Player;
import com.drawit.demo.model.enums.EventMessageType;
import com.drawit.demo.websocket.GameEventMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class GameMessagingServiceImpl implements GameMessagingService {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public GameMessagingServiceImpl(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // send drawer's word
    public void sendCurrentWord(Player player, Game game) {
        GameEventMessage gameEventMessage = new GameEventMessage(EventMessageType.CURRENT_WORD, game.getCurrWord());
        messagingTemplate.convertAndSend("/topic/game/" + game.getGameID() + "/event/player/" + player.getId()
                , gameEventMessage);
    }

    // send when round starts
    public void sendRoundStart(Game game) {
        GameEventMessage gameEventMessage = new GameEventMessage(EventMessageType.ROUND_START,
                String.valueOf(game.getCurrRound()));
        messagingTemplate.convertAndSend("/topic/game/" + game.getGameID() + "/event", gameEventMessage);
    }

    public void sendRoundOver(Game game) {
        GameEventMessage gameEventMessage = new GameEventMessage(EventMessageType.ROUND_END);
        messagingTemplate.convertAndSend("/topic/game/" + game.getGameID() + "/event", gameEventMessage);
    }

    public void sendCorrectGuessAnnouncement(Player player, Game game) {
        GameEventMessage gameEventMessage = new GameEventMessage(EventMessageType.CORRECT_GUESS_ANNOUNCEMENT,
                player.getUsername());
        messagingTemplate.convertAndSend("/topic/game/" + game.getGameID() + "/event", gameEventMessage);
    }

    public void sendGameOver(Game game) {
        GameEventMessage gameEventMessage = new GameEventMessage(EventMessageType.GAME_OVER);
        messagingTemplate.convertAndSend("/topic/game/" + game.getGameID() + "/event", gameEventMessage);
    }
}

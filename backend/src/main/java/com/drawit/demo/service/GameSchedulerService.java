package com.drawit.demo.service;

import com.drawit.demo.model.Game;
import com.drawit.demo.model.ScheduledTask;

import java.util.UUID;

public interface GameSchedulerService {
    void createTask(UUID gameID);

    void stopGame(Game game);

    void advanceTurnNow(Game game);

    void startOrAdvanceTurn(Game game, ScheduledTask task);
}

package com.drawit.demo.service;

import com.drawit.demo.model.Game;

import java.util.UUID;

public interface GameSchedulerService {
    void createTask(UUID gameID);

    void stopGame(Game game);
}

package com.drawit.demo.service;

import com.drawit.demo.model.Game;
import com.drawit.demo.model.ScheduledTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.drawit.demo.model.ScheduledTask;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

@Service
public class GameSchedulerServiceImpl implements GameSchedulerService {
    private final Map<UUID, ScheduledTask> gameScheduler = new ConcurrentHashMap<>();
    private final GameService gameService;
    private final GameMessagingService gameMessagingService;
    private final GameLogicService gameLogicService;

    @Autowired
    public GameSchedulerServiceImpl(GameService gameService, GameMessagingService gameMessagingService,
                                    GameLogicService gameLogicService) {
        this.gameService = gameService;
        this.gameMessagingService = gameMessagingService;
        this.gameLogicService = gameLogicService;
    }

    public void createTask(UUID gameID) {
        if (gameScheduler.containsKey(gameID)) {
            return;
        }
        Game game = gameService.getGame(gameID);

        Runnable task = () -> {
            game.setGameStarted(true);
            gameMessagingService.sendRoundStart(game);
            gameLogicService.incrementRound(game);

            if (gameLogicService.isGameFinished(game)) {
                stopGame(game);
            }
            gameLogicService.nextPlayer(game.getCounter(), game);
            game.setCounter(game.getCounter() + 1);
        };

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        ScheduledFuture<?> future = executor.scheduleAtFixedRate(task, 0, 120, TimeUnit.SECONDS);
        ScheduledTask scheduledTask = new ScheduledTask(executor, future);
        gameScheduler.put(gameID, scheduledTask);
    }

    public void stopGame(Game game) {
        UUID gameID = game.getGameID();
        ScheduledTask task = gameScheduler.remove(gameID);
        if (task != null) {
            task.cancel();
        }
    }
}

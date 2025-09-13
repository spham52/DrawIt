package com.drawit.demo.service;

import com.drawit.demo.model.Game;
import com.drawit.demo.model.ScheduledTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

@Service
public class GameSchedulerServiceImpl implements GameSchedulerService {
    private final Map<UUID, ScheduledTask> gameScheduler = new ConcurrentHashMap<>();
    private final GameService gameService;
    private final GameLogicService gameLogicService;

    @Autowired
    public GameSchedulerServiceImpl(GameService gameService,
                                    GameLogicService gameLogicService) {
        this.gameService = gameService;
        this.gameLogicService = gameLogicService;
    }

    public void createTask(UUID gameID) {
        Game game = gameService.getGame(gameID);

        if (gameScheduler.containsKey(gameID) || game.getPlayers().size() < 2) {
            return;
        }

        Runnable task = buildTask(game);

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        ScheduledFuture<?> future = executor.scheduleAtFixedRate(task, 1, 120, TimeUnit.SECONDS);
        ScheduledTask scheduledTask = new ScheduledTask(executor, future);
        gameScheduler.put(gameID, scheduledTask);
    }

    public void stopGame(Game game) {
        UUID gameID = game.getGameID();
        ScheduledTask task = gameScheduler.remove(gameID);
        if (task != null) {
            task.cancel();
        }
        if (game.getPlayers().size() > 2) {
            Game newGame = gameService.restartGame(game);
            createTask(newGame.getGameID());
        }
    }

    public void rescheduleTask(Game game) {
        ScheduledTask oldTask = gameScheduler.get(game.getGameID());
        if (oldTask != null) {
            oldTask.cancel();
            Runnable task = buildTask(game);
            ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
            ScheduledFuture<?> future = executor.scheduleAtFixedRate(task, 1, 120, TimeUnit.SECONDS);
            ScheduledTask scheduledTask = new ScheduledTask(executor, future);
            gameScheduler.put(game.getGameID(), scheduledTask);
        }
    }

    public Runnable buildTask(Game game) {
        return () -> {
            gameLogicService.incrementRound(game);

            gameLogicService.nextPlayer(game);

            if (!game.isGameStarted() && game.getDrawer() != null) {
                game.setGameStarted(true);
            }

            if (gameLogicService.isGameFinished(game)) {
                stopGame(game);
            }
        };
    }
}

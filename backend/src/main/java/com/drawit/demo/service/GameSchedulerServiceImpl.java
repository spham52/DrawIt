package com.drawit.demo.service;

import com.drawit.demo.model.Game;
import com.drawit.demo.model.ScheduledTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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

        if (game == null || game.getPlayers().size() < 2) return;

        if (!gameScheduler.containsKey(gameID)) {
            synchronized (gameScheduler) {
                ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
                ScheduledTask task = new ScheduledTask(exec, null);
                gameScheduler.put(gameID, task);

                task.setFuture(exec.schedule(() -> startOrAdvanceTurn(game, task),
                        2, TimeUnit.SECONDS));
            }
        }
    }

    public void stopGame(Game game) {
        UUID gameID = game.getGameID();
        ScheduledTask task = gameScheduler.remove(gameID);
        if (task != null) {
            task.cancel();
        }

        if (game.getPlayers().size() >= 2) {
            Game newGame = gameService.restartGame(game);
            createTask(newGame.getGameID());
        }
    }

    public void startOrAdvanceTurn(Game game, ScheduledTask task) {
        gameLogicService.incrementRound(game);

        if (gameLogicService.isGameFinished(game)) {
            stopGame(game);
            return;
        }
        gameLogicService.nextPlayer(game);

        if (!game.isGameStarted() && game.getDrawer() != null) {
            game.setGameStarted(true);
        }

        if (task.getFuture() != null) task.getFuture().cancel(true);
        task.setFuture(task.getExecutor().schedule(() ->
                startOrAdvanceTurn(game, task),
                120,
                TimeUnit.SECONDS));
    }

    public void advanceTurnNow(Game game) {
        ScheduledTask task = gameScheduler.get(game.getGameID());
        if (task == null) return;
        if (task.getFuture() != null) task.getFuture().cancel(true);
        task.getExecutor().execute(() -> startOrAdvanceTurn(game, task));
    }
}

package pt.uevora.joker.app.ui;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import pt.uevora.joker.game.JogoDoJoker;

public class GameRunner {
    private final ExecutorService executor;
    private final GameIO io;
    private final AtomicBoolean started;

    public GameRunner(GameIO io) {
        this.executor = Executors.newSingleThreadExecutor();
        this.io = io;
        this.started = new AtomicBoolean(false);
    }

    public void start() {
        if (!started.compareAndSet(false, true)) {
            return;
        }
        executor.submit(() -> {
            try {
                new JogoDoJoker(io).jogar();
            } catch (RuntimeException e) {
                String message = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
                io.showError("Error: " + message);
            }
        });
    }

    public void shutdown() {
        executor.shutdownNow();
    }
}

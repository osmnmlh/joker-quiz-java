package pt.uevora.joker.app.ui.swing;

import java.util.concurrent.CompletableFuture;

import javax.swing.SwingUtilities;

import pt.uevora.joker.app.ui.GameIO;
import pt.uevora.joker.domain.EstadoJogador;
import pt.uevora.joker.domain.PerguntaNormal;
import pt.uevora.joker.game.mechanics.PerguntaNormalSession;

public class SwingGameIO implements GameIO {
    private static final String NOT_IMPLEMENTED_MESSAGE =
            "GUI gameplay screens not implemented yet (Step 2.4+).";
    private final MainMenuFrame frame;

    public SwingGameIO(MainMenuFrame frame) {
        this.frame = frame;
    }

    @Override
    public void showInfo(String message) {
        appendStatus(message);
    }

    @Override
    public void showWarning(String message) {
        appendStatus(message);
    }

    @Override
    public void showError(String message) {
        appendStatus(message);
    }

    @Override
    public void showNormalQuestion(int roundNumber, PerguntaNormal pergunta, PerguntaNormalSession session,
                                   EstadoJogador estado) {
        appendStatus("Round " + roundNumber + ": " + pergunta.getEnunciado());
    }

    @Override
    public CompletableFuture<Boolean> requestUseJokerAsync(EstadoJogador estado, PerguntaNormalSession session) {
        return notImplemented();
    }

    @Override
    public CompletableFuture<Integer> requestAnswerIndexAsync(PerguntaNormal pergunta, PerguntaNormalSession session,
                                                             EstadoJogador estado, int roundNumber) {
        return notImplemented();
    }

    @Override
    public CompletableFuture<Boolean> requestStopFinalRoundAsync(EstadoJogador estado) {
        return notImplemented();
    }

    private void appendStatus(String message) {
        SwingUtilities.invokeLater(() -> frame.appendStatus(message));
    }

    private <T> CompletableFuture<T> notImplemented() {
        CompletableFuture<T> future = new CompletableFuture<>();
        future.completeExceptionally(new IllegalStateException(NOT_IMPLEMENTED_MESSAGE));
        return future;
    }
}

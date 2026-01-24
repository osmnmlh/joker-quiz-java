package pt.uevora.joker.app.ui;

import java.util.concurrent.CompletableFuture;

import pt.uevora.joker.domain.EstadoJogador;
import pt.uevora.joker.domain.PerguntaNormal;
import pt.uevora.joker.game.mechanics.PerguntaNormalSession;

public interface GameIO {
    void showInfo(String message);

    void showWarning(String message);

    void showError(String message);

    void showNormalQuestion(int roundNumber, PerguntaNormal pergunta, PerguntaNormalSession session,
                            EstadoJogador estado);

    CompletableFuture<Boolean> requestUseJokerAsync(EstadoJogador estado, PerguntaNormalSession session);

    CompletableFuture<Integer> requestAnswerIndexAsync(PerguntaNormal pergunta, PerguntaNormalSession session,
                                                      EstadoJogador estado, int roundNumber);

    CompletableFuture<Boolean> requestStopFinalRoundAsync(EstadoJogador estado);
}

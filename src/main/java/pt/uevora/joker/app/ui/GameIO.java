package pt.uevora.joker.app.ui;

import java.util.concurrent.CompletableFuture;

import pt.uevora.joker.domain.EstadoJogador;
import pt.uevora.joker.domain.PerguntaBonus;
import pt.uevora.joker.domain.PerguntaNormal;
import pt.uevora.joker.game.mechanics.PerguntaNormalSession;

public interface GameIO {
    void showInfo(String message);

    void showWarning(String message);

    void showError(String message);

    void showNormalQuestion(int roundNumber, PerguntaNormal pergunta, PerguntaNormalSession session,
                            EstadoJogador estado);

    void showBonusStart(EstadoJogador estado);

    void showBonusProgress(int correct);

    void showBonusSummary(int correct, int jokersGained, EstadoJogador estado);

    void showFinalSummary(int prize, int jokers);

    CompletableFuture<Boolean> requestUseJokerAsync(EstadoJogador estado, PerguntaNormalSession session);

    CompletableFuture<Integer> requestAnswerIndexAsync(PerguntaNormal pergunta, PerguntaNormalSession session,
                                                      EstadoJogador estado, int roundNumber);

    CompletableFuture<Integer> requestBonusAnswerIndexAsync(PerguntaBonus pergunta, long remainingMs);

    CompletableFuture<Boolean> requestStopFinalRoundAsync(EstadoJogador estado);
}

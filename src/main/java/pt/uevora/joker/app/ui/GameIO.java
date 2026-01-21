package pt.uevora.joker.app.ui;

import pt.uevora.joker.domain.EstadoJogador;
import pt.uevora.joker.domain.PerguntaNormal;
import pt.uevora.joker.game.mechanics.PerguntaNormalSession;

public interface GameIO {
    void showInfo(String message);

    void showWarning(String message);

    void showError(String message);

    void showNormalQuestion(int roundNumber, PerguntaNormal pergunta, PerguntaNormalSession session,
                            EstadoJogador estado);

    boolean requestUseJoker(EstadoJogador estado, PerguntaNormalSession session);

    int requestAnswerIndex(PerguntaNormalSession session);

    boolean requestStopFinalRound(EstadoJogador estado);
}

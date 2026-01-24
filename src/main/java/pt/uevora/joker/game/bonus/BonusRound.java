package pt.uevora.joker.game.bonus;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import pt.uevora.joker.app.ui.GameIO;
import pt.uevora.joker.domain.EstadoJogador;
import pt.uevora.joker.domain.PerguntaBonus;

public class BonusRound {
    private static final long BONUS_DURATION_MS = 60_000L;

    public void executar(EstadoJogador estado, BonusQuestionBank banco, GameIO io) throws IOException {
        if (banco.isEmpty()) {
            io.showInfo("Bonus skipped: no bonus questions available.");
            return;
        }

        io.showBonusStart(estado);
        long endTime = System.currentTimeMillis() + BONUS_DURATION_MS;
        int corretas = 0;

        while (System.currentTimeMillis() < endTime) {
            if (banco.isEmpty()) {
                io.showInfo("Bonus ended early: no more questions.");
                break;
            }
            PerguntaBonus pergunta = banco.next();
            long remainingMs = endTime - System.currentTimeMillis();
            if (remainingMs <= 0) {
                break;
            }

            try {
                int indice = io.requestBonusAnswerIndexAsync(pergunta, remainingMs)
                        .get(remainingMs, TimeUnit.MILLISECONDS);
                if (pergunta.validarResposta(indice)) {
                    corretas++;
                    io.showInfo("Correct!");
                } else {
                    io.showInfo("Wrong.");
                }
                io.showBonusProgress(corretas);
            } catch (TimeoutException e) {
                io.showInfo("Time is up!");
                break;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                io.showInfo("Time is up!");
                break;
            } catch (ExecutionException e) {
                Throwable cause = e.getCause();
                if (cause instanceof TimeoutException) {
                    io.showInfo("Time is up!");
                    break;
                }
                throw new IOException("Failed to read bonus input", cause);
            }
        }

        int jokersGanhos = corretas / 5;
        if (jokersGanhos > 0) {
            estado.ajustarJokers(jokersGanhos);
        }
        io.showBonusSummary(corretas, jokersGanhos, estado);
    }
}

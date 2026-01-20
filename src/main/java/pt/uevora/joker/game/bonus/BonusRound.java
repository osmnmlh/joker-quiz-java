package pt.uevora.joker.game.bonus;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import pt.uevora.joker.domain.EstadoJogador;
import pt.uevora.joker.domain.PerguntaBonus;

public class BonusRound {
    private static final long BONUS_DURATION_MS = 60_000L;

    public void executar(EstadoJogador estado, BonusQuestionBank banco, BufferedReader reader) throws IOException {
        if (banco.isEmpty()) {
            System.out.println("Bonus skipped: no bonus questions available.");
            return;
        }

        System.out.println("=== Bonus Round (60 seconds) ===");
        long endTime = System.currentTimeMillis() + BONUS_DURATION_MS;
        int corretas = 0;

        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            while (System.currentTimeMillis() < endTime) {
                if (banco.isEmpty()) {
                    System.out.println("Bonus ended early: no more questions.");
                    break;
                }
                PerguntaBonus pergunta = banco.next();
                System.out.println("Bonus question: " + pergunta.getEnunciado());
                System.out.println("A. " + pergunta.getOpcoes().get(0));
                System.out.println("B. " + pergunta.getOpcoes().get(1));

                long remainingMs = endTime - System.currentTimeMillis();
                if (remainingMs <= 0) {
                    break;
                }

                // Best-effort timing: console input may block; we avoid starting a new question after timeout.
                Optional<Integer> indice = solicitarRespostaBonus(reader, executor, endTime);
                if (!indice.isPresent()) {
                    System.out.println("Time is up!");
                    break;
                }
                if (pergunta.validarResposta(indice.get())) {
                    corretas++;
                    System.out.println("Correct!");
                } else {
                    System.out.println("Wrong.");
                }

            }
        } finally {
            executor.shutdownNow();
        }

        int jokersGanhos = corretas / 5;
        if (jokersGanhos > 0) {
            estado.ajustarJokers(jokersGanhos);
        }
        System.out.println("Bonus correct answers: " + corretas);
        System.out.println("Jokers awarded: " + jokersGanhos);
    }

    private Optional<String> readLineWithTimeout(BufferedReader reader, ExecutorService executor, long timeoutMs)
            throws IOException {
        Callable<String> task = reader::readLine;
        Future<String> future = executor.submit(task);
        try {
            String line = future.get(timeoutMs, TimeUnit.MILLISECONDS);
            return Optional.ofNullable(line);
        } catch (TimeoutException e) {
            future.cancel(true);
            // Console input may not be interruptible; if so, the current question may finish after timeout.
            return Optional.empty();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return Optional.empty();
        } catch (ExecutionException e) {
            throw new IOException("Failed to read bonus input", e.getCause());
        }
    }

    private Optional<Integer> solicitarRespostaBonus(BufferedReader reader, ExecutorService executor, long endTime)
            throws IOException {
        while (System.currentTimeMillis() < endTime) {
            long remainingMs = endTime - System.currentTimeMillis();
            if (remainingMs <= 0) {
                break;
            }
            // Best-effort timing: console input may block; we avoid starting a new question after timeout.
            Optional<String> resposta = readLineWithTimeout(reader, executor, remainingMs);
            if (!resposta.isPresent()) {
                return Optional.empty();
            }
            int indice = letraParaIndice(resposta.get().trim().toUpperCase());
            if (indice == -1) {
                System.out.println("Invalid answer. Please enter A or B.");
                continue;
            }
            return Optional.of(indice);
        }
        return Optional.empty();
    }

    private int letraParaIndice(String letra) {
        if (letra == null || letra.length() != 1) {
            return -1;
        }
        switch (letra.charAt(0)) {
            case 'A':
                return 0;
            case 'B':
                return 1;
            default:
                return -1;
        }
    }
}

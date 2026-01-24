package pt.uevora.joker.app.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.CompletableFuture;

import pt.uevora.joker.domain.EstadoJogador;
import pt.uevora.joker.domain.PerguntaBonus;
import pt.uevora.joker.domain.PerguntaNormal;
import pt.uevora.joker.game.mechanics.PerguntaNormalSession;

public class ConsoleGameIO implements GameIO {
    private final BufferedReader reader;

    public ConsoleGameIO() {
        this(new BufferedReader(new InputStreamReader(System.in)));
    }

    public ConsoleGameIO(BufferedReader reader) {
        this.reader = reader;
    }

    @Override
    public void showInfo(String message) {
        System.out.println(message);
    }

    @Override
    public void showWarning(String message) {
        System.out.println(message);
    }

    @Override
    public void showError(String message) {
        System.err.println(message);
    }

    @Override
    public void showNormalQuestion(int roundNumber, PerguntaNormal pergunta, PerguntaNormalSession session,
                                   EstadoJogador estado) {
        System.out.println("Question: " + pergunta.getEnunciado());
        showRemainingOptions(pergunta, session);
    }

    @Override
    public void showBonusStart(EstadoJogador estado) {
        System.out.println("=== Bonus Round (60 seconds) ===");
    }

    @Override
    public void showBonusProgress(int correct) {
        // No-op for console to preserve existing output cadence.
    }

    @Override
    public void showBonusSummary(int correct, int jokersGained, EstadoJogador estado) {
        System.out.println("Bonus correct answers: " + correct);
        System.out.println("Jokers awarded: " + jokersGained);
    }

    @Override
    public void showFinalSummary(int prize, int jokers, String endReason) {
        System.out.println("Final prize: " + prize);
        System.out.println("Final jokers: " + jokers);
        if (endReason != null && !endReason.trim().isEmpty()) {
            System.out.println(endReason);
        }
    }

    @Override
    public CompletableFuture<Boolean> requestUseJokerAsync(EstadoJogador estado, PerguntaNormalSession session) {
        return CompletableFuture.completedFuture(requestUseJokerSync());
    }

    @Override
    public CompletableFuture<Integer> requestAnswerIndexAsync(PerguntaNormal pergunta, PerguntaNormalSession session,
                                                             EstadoJogador estado, int roundNumber) {
        return CompletableFuture.completedFuture(requestAnswerIndexSync(session));
    }

    @Override
    public CompletableFuture<Integer> requestBonusAnswerIndexAsync(PerguntaBonus pergunta, long remainingMs) {
        return CompletableFuture.completedFuture(requestBonusAnswerIndexSync(pergunta));
    }

    @Override
    public CompletableFuture<Boolean> requestStopFinalRoundAsync(EstadoJogador estado) {
        return CompletableFuture.completedFuture(requestStopFinalRoundSync());
    }

    private void showRemainingOptions(PerguntaNormal pergunta, PerguntaNormalSession session) {
        for (Integer indice : session.getOpcoesRestantes()) {
            char letra = (char) ('A' + indice);
            System.out.println(letra + ". " + pergunta.getOpcoes().get(indice));
        }
    }

    private boolean requestUseJokerSync() {
        while (true) {
            System.out.print("Use a joker to remove one option? (y/n): ");
            String input = readLine();
            String normalized = input.trim().toLowerCase();
            if (normalized.equals("y") || normalized.equals("yes")) {
                return true;
            }
            if (normalized.equals("n") || normalized.equals("no")) {
                return false;
            }
            System.out.println("Please enter y or n.");
        }
    }

    private int requestAnswerIndexSync(PerguntaNormalSession session) {
        while (true) {
            System.out.print("Choose your answer (A/B/C/D): ");
            String input = readLine();
            String normalized = input.trim().toUpperCase();
            if (normalized.length() != 1) {
                System.out.println("Enter a single letter.");
                continue;
            }
            int indice = letterToIndex(normalized.charAt(0));
            if (indice == -1) {
                System.out.println("Enter A, B, C, or D.");
                continue;
            }
            if (!session.getOpcoesRestantes().contains(indice)) {
                System.out.println("That option has been eliminated. Choose from remaining options.");
                continue;
            }
            return indice;
        }
    }

    private boolean requestStopFinalRoundSync() {
        while (true) {
            System.out.print("Final round: do you want to STOP and keep your prize? (y/n): ");
            String input = readLine();
            String normalized = input.trim().toLowerCase();
            if (normalized.equals("y") || normalized.equals("yes") || normalized.equals("stop")) {
                return true;
            }
            if (normalized.equals("n") || normalized.equals("no")) {
                return false;
            }
            System.out.println("Please enter y or n.");
        }
    }

    private int requestBonusAnswerIndexSync(PerguntaBonus pergunta) {
        System.out.println("Bonus question: " + pergunta.getEnunciado());
        System.out.println("A. " + pergunta.getOpcoes().get(0));
        System.out.println("B. " + pergunta.getOpcoes().get(1));
        while (true) {
            String input = readLine();
            String normalized = input.trim().toUpperCase();
            int indice = bonusLetterToIndex(normalized);
            if (indice == -1) {
                System.out.println("Invalid answer. Please enter A or B.");
                continue;
            }
            return indice;
        }
    }

    private int letterToIndex(char letra) {
        switch (letra) {
            case 'A':
                return 0;
            case 'B':
                return 1;
            case 'C':
                return 2;
            case 'D':
                return 3;
            default:
                return -1;
        }
    }

    private int bonusLetterToIndex(String letra) {
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

    private String readLine() {
        try {
            String line = reader.readLine();
            if (line == null) {
                throw new IllegalStateException("Input stream closed");
            }
            return line;
        } catch (IOException e) {
            showError("Error: " + e.getMessage());
            return null;
        }
    }
}

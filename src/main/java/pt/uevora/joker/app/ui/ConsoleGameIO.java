package pt.uevora.joker.app.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import pt.uevora.joker.domain.EstadoJogador;
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
    public boolean requestUseJoker(EstadoJogador estado, PerguntaNormalSession session) {
        while (true) {
            System.out.print("Use a joker to remove one option? (y/n): ");
            String input = readLine();
            if (input == null) {
                return false;
            }
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

    @Override
    public int requestAnswerIndex(PerguntaNormalSession session) {
        while (true) {
            System.out.print("Choose your answer (A/B/C/D): ");
            String input = readLine();
            if (input == null) {
                continue;
            }
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

    @Override
    public boolean requestStopFinalRound(EstadoJogador estado) {
        while (true) {
            System.out.print("Final round: do you want to STOP and keep your prize? (y/n): ");
            String input = readLine();
            if (input == null) {
                return false;
            }
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

    private void showRemainingOptions(PerguntaNormal pergunta, PerguntaNormalSession session) {
        for (Integer indice : session.getOpcoesRestantes()) {
            char letra = (char) ('A' + indice);
            System.out.println(letra + ". " + pergunta.getOpcoes().get(indice));
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

    private String readLine() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            showError("Error: " + e.getMessage());
            return null;
        }
    }
}

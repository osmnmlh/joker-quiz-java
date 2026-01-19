package pt.uevora.joker.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import pt.uevora.joker.domain.EstadoJogador;
import pt.uevora.joker.domain.MoneyLevels;
import pt.uevora.joker.domain.PerguntaNormal;
import pt.uevora.joker.game.mechanics.JokerMechanics;
import pt.uevora.joker.game.mechanics.PerguntaNormalSession;
import pt.uevora.joker.io.QuestionBankBootstrap;

public class JogoDoJoker {
    private static final int TOTAL_ROUNDS = 12;
    private static final int START_LEVEL_INDEX = 0;
    private static final int START_JOKERS = 7;
    // Policy: when N < 3 on wrong answer, all remaining jokers are lost (Option A).
    private static final boolean RESET_JOKERS_ON_PENALTY = true;

    public void jogar() {
        try {
            Map<Integer, List<PerguntaNormal>> perguntasCarregadas = QuestionBankBootstrap.carregarPerguntasNormais();
            NormalQuestionBank banco = new NormalQuestionBank(perguntasCarregadas);
            executarJogo(banco);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void executarJogo(NormalQuestionBank banco) throws IOException {
        EstadoJogador estado = new EstadoJogador(START_LEVEL_INDEX, START_JOKERS);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        for (int round = 1; round <= TOTAL_ROUNDS; round++) {
            if (round == TOTAL_ROUNDS && desejaParar(reader)) {
                estado.ajustarNivelDinheiro(-1);
                break;
            }

            int indiceNivel = estado.getIndiceNivelDinheiro();
            int valorNivel = MoneyLevels.LEVELS[indiceNivel];
            PerguntaNormal pergunta = banco.getNextQuestionForLevel(valorNivel);
            PerguntaNormalSession session = new PerguntaNormalSession(pergunta);

            exibirPergunta(pergunta, session);
            while (JokerMechanics.canApplyJoker(session, estado.getQuantidadeJokers())
                    && desejaUsarJoker(reader)) {
                JokerMechanics.applyOneJoker(session, estado);
                exibirOpcoesRestantes(pergunta, session);
            }

            int resposta = solicitarResposta(reader, session);
            boolean correta = pergunta.validarResposta(resposta);
            if (correta) {
                estado.avancarNivelDinheiro();
                System.out.println("Correct! Moving up a level.");
            } else {
                aplicarPenalidadePorErro(estado);
                System.out.println("Wrong answer.");
            }
        }

        int premio = MoneyLevels.LEVELS[estado.getIndiceNivelDinheiro()];
        System.out.println("Final prize: " + premio);
        System.out.println("Final jokers: " + estado.getQuantidadeJokers());
    }

    private boolean desejaParar(BufferedReader reader) throws IOException {
        while (true) {
            System.out.print("Final round: do you want to STOP and keep your prize? (y/n): ");
            String input = reader.readLine();
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

    private boolean desejaUsarJoker(BufferedReader reader) throws IOException {
        while (true) {
            System.out.print("Use a joker to remove one option? (y/n): ");
            String input = reader.readLine();
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

    private int solicitarResposta(BufferedReader reader, PerguntaNormalSession session) throws IOException {
        while (true) {
            System.out.print("Choose your answer (A/B/C/D): ");
            String input = reader.readLine();
            if (input == null) {
                continue;
            }
            String normalized = input.trim().toUpperCase();
            if (normalized.length() != 1) {
                System.out.println("Enter a single letter.");
                continue;
            }
            int indice = letraParaIndice(normalized.charAt(0));
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

    private int letraParaIndice(char letra) {
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

    private void aplicarPenalidadePorErro(EstadoJogador estado) {
        int jokers = estado.getQuantidadeJokers();
        if (jokers >= 3) {
            estado.ajustarJokers(-3);
            return;
        }

        int indiceAtual = estado.getIndiceNivelDinheiro();
        int novoIndice = indiceAtual - (3 - jokers);
        int delta = novoIndice - indiceAtual;
        estado.ajustarNivelDinheiro(delta);

        if (RESET_JOKERS_ON_PENALTY) {
            estado.ajustarJokers(-jokers);
        }
    }

    private void exibirPergunta(PerguntaNormal pergunta, PerguntaNormalSession session) {
        System.out.println("Question: " + pergunta.getEnunciado());
        exibirOpcoesRestantes(pergunta, session);
    }

    private void exibirOpcoesRestantes(PerguntaNormal pergunta, PerguntaNormalSession session) {
        for (Integer indice : session.getOpcoesRestantes()) {
            char letra = (char) ('A' + indice);
            System.out.println(letra + ". " + pergunta.getOpcoes().get(indice));
        }
    }
}

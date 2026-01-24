package pt.uevora.joker.game;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import pt.uevora.joker.app.ui.GameIO;
import pt.uevora.joker.domain.EstadoJogador;
import pt.uevora.joker.domain.MoneyLevels;
import pt.uevora.joker.domain.PerguntaBonus;
import pt.uevora.joker.domain.PerguntaNormal;
import pt.uevora.joker.game.bonus.BonusQuestionBank;
import pt.uevora.joker.game.bonus.BonusRound;
import pt.uevora.joker.game.mechanics.JokerMechanics;
import pt.uevora.joker.game.mechanics.PerguntaNormalSession;
import pt.uevora.joker.io.QuestionBankBootstrap;

public class JogoDoJoker {
    private static final int TOTAL_ROUNDS = 12;
    private static final int START_LEVEL_INDEX = 1;
    private static final int START_JOKERS = 7;
    // Policy: when N < 3 on wrong answer, all remaining jokers are lost (Option A).
    private static final boolean RESET_JOKERS_ON_PENALTY = true;
    private final GameIO io;

    public JogoDoJoker(GameIO io) {
        this.io = Objects.requireNonNull(io, "io");
    }

    public void jogar() {
        EstadoJogador estado = new EstadoJogador(START_LEVEL_INDEX, START_JOKERS);
        String endReason = "Completed all rounds";
        try {
            Map<Integer, List<PerguntaNormal>> perguntasCarregadas = QuestionBankBootstrap.carregarPerguntasNormais();
            NormalQuestionBank banco = new NormalQuestionBank(perguntasCarregadas);
            endReason = executarJogo(banco, estado);
        } catch (IOException e) {
            io.showError("Error: " + e.getMessage());
            endReason = "Game ended due to missing questions";
        } catch (IllegalStateException e) {
            io.showError("Error: " + e.getMessage());
            endReason = "Game ended due to missing questions";
        }
        int premio = MoneyLevels.LEVELS[estado.getIndiceNivelDinheiro()];
        io.showFinalSummary(premio, estado.getQuantidadeJokers(), endReason);
    }

    private String executarJogo(NormalQuestionBank banco, EstadoJogador estado) throws IOException {
        for (int round = 1; round <= TOTAL_ROUNDS; round++) {
            if (round == TOTAL_ROUNDS && io.requestStopFinalRoundAsync(estado).join()) {
                estado.ajustarNivelDinheiro(-1);
                return "Stopped on final round (−1 level applied)";
            }

            int indiceNivel = estado.getIndiceNivelDinheiro();
            int targetIndex = Math.min(indiceNivel + 1, MoneyLevels.maxIndex());
            int valorNivel = MoneyLevels.LEVELS[targetIndex];
            PerguntaNormal pergunta = banco.getNextQuestionForLevel(valorNivel);
            PerguntaNormalSession session = new PerguntaNormalSession(pergunta);

            io.showNormalQuestion(round, pergunta, session, estado);
            while (JokerMechanics.canApplyJoker(session, estado.getQuantidadeJokers())
                    && io.requestUseJokerAsync(estado, session).join()) {
                JokerMechanics.applyOneJoker(session, estado);
                io.showNormalQuestion(round, pergunta, session, estado);
            }

            int resposta = io.requestAnswerIndexAsync(pergunta, session, estado, round).join();
            boolean correta = pergunta.validarResposta(resposta);
            if (correta) {
                estado.avancarNivelDinheiro();
                io.showInfo("Correct! Moving up a level.");
            } else {
                aplicarPenalidadePorErro(estado);
                io.showInfo("Wrong answer.");
            }

            if (round == 4 || round == 8) {
                executarBonusSeDisponivel(estado);
            }
        }
        return "Completed all rounds";
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

    private void executarBonusSeDisponivel(EstadoJogador estado) throws IOException {
        List<PerguntaBonus> bonusPerguntas = carregarPerguntasBonus();
        if (bonusPerguntas.isEmpty()) {
            io.showWarning("Warning: bonus round skipped because no bonus questions are available.");
            return;
        }
        BonusQuestionBank banco = new BonusQuestionBank(bonusPerguntas);
        new BonusRound().executar(estado, banco, io);
    }

    private List<PerguntaBonus> carregarPerguntasBonus() throws IOException {
        return QuestionBankBootstrap.carregarPerguntasBonus();
    }
}

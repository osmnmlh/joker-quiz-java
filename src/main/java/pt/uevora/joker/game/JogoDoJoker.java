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
import pt.uevora.joker.io.QuestionCache;
import pt.uevora.joker.io.QuestionPaths;

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
        try {
            Map<Integer, List<PerguntaNormal>> perguntasCarregadas = QuestionBankBootstrap.carregarPerguntasNormais();
            NormalQuestionBank banco = new NormalQuestionBank(perguntasCarregadas);
            executarJogo(banco);
        } catch (IOException e) {
            io.showError("Error: " + e.getMessage());
        } catch (IllegalStateException e) {
            io.showError("Error: " + e.getMessage());
        }
    }

    private void executarJogo(NormalQuestionBank banco) throws IOException {
        EstadoJogador estado = new EstadoJogador(START_LEVEL_INDEX, START_JOKERS);

        for (int round = 1; round <= TOTAL_ROUNDS; round++) {
            if (round == TOTAL_ROUNDS && io.requestStopFinalRoundAsync(estado).join()) {
                estado.ajustarNivelDinheiro(-1);
                break;
            }

            int indiceNivel = estado.getIndiceNivelDinheiro();
            int valorNivel = MoneyLevels.LEVELS[indiceNivel];
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

        int premio = MoneyLevels.LEVELS[estado.getIndiceNivelDinheiro()];
        io.showFinalSummary(premio, estado.getQuantidadeJokers());
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
        if (java.nio.file.Files.exists(QuestionPaths.bonusCacheFile())) {
            return QuestionCache.loadPerguntasBonus();
        }

        if (QuestionPaths.findBonusTextFile().isPresent()) {
            io.showWarning("Warning: bonus question file found but parsing is not implemented yet.");
        } else {
            io.showWarning("Warning: bonus question file/cache missing; bonus rounds will be skipped.");
        }
        return java.util.Collections.emptyList();
    }
}

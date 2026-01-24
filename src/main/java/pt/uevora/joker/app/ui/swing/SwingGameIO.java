package pt.uevora.joker.app.ui.swing;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import pt.uevora.joker.app.ui.GameIO;
import pt.uevora.joker.domain.EstadoJogador;
import pt.uevora.joker.domain.MoneyLevels;
import pt.uevora.joker.domain.PerguntaBonus;
import pt.uevora.joker.domain.PerguntaNormal;
import pt.uevora.joker.game.mechanics.PerguntaNormalSession;

public class SwingGameIO implements GameIO {
    private final GameFrame frame;
    private final NormalQuestionPanel normalPanel;
    private final BonusPanel bonusPanel;
    private final FinalPanel finalPanel;

    private volatile CompletableFuture<Boolean> pendingJokerDecision;
    private volatile CompletableFuture<Integer> pendingAnswer;
    private volatile CompletableFuture<Integer> pendingBonusAnswer;
    private volatile CompletableFuture<Boolean> pendingStopDecision;

    private PerguntaNormal currentQuestion;
    private Set<Integer> lastRemaining;
    private int correctBonusCount;
    private int lastRoundNumber;

    public SwingGameIO(GameFrame frame) {
        this.frame = frame;
        this.normalPanel = frame.getNormalPanel();
        this.bonusPanel = frame.getBonusPanel();
        this.finalPanel = frame.getFinalPanel();
        wireNormalPanelActions();
        wireBonusPanelActions();
    }

    @Override
    public void showInfo(String message) {
        frame.appendMessage(message);
    }

    @Override
    public void showWarning(String message) {
        frame.appendMessage(message);
    }

    @Override
    public void showError(String message) {
        frame.appendMessage(message);
    }

    @Override
    public void showNormalQuestion(int roundNumber, PerguntaNormal pergunta, PerguntaNormalSession session,
                                   EstadoJogador estado) {
        SwingUtilities.invokeLater(() -> {
            lastRoundNumber = roundNumber;
            updateStatus(roundNumber, estado);
            bonusPanel.stopTimer();
            frame.showNormalCard();
            normalPanel.setQuestion(pergunta.getEnunciado());
            Set<Integer> remaining = new HashSet<>(session.getOpcoesRestantes());
            normalPanel.setOptions(pergunta.getOpcoes(), remaining);
            normalPanel.setJokerEnabled(estado.getQuantidadeJokers() > 0);
            normalPanel.setJokerDecisionMode(false);
            detectJokerElimination(pergunta, remaining);
        });
    }

    @Override
    public void showBonusStart(EstadoJogador estado) {
        SwingUtilities.invokeLater(() -> {
            correctBonusCount = 0;
            updateStatus(lastRoundNumber, estado);
            frame.showBonusCard();
            bonusPanel.reset();
            bonusPanel.setCorrectCount(correctBonusCount);
        });
    }

    @Override
    public void showBonusProgress(int correct) {
        SwingUtilities.invokeLater(() -> bonusPanel.setCorrectCount(correct));
    }

    @Override
    public void showBonusSummary(int correct, int jokersGained, EstadoJogador estado) {
        SwingUtilities.invokeLater(() -> {
            bonusPanel.stopTimer();
            frame.appendMessage("Bonus correct answers: " + correct);
            frame.appendMessage("Jokers awarded: " + jokersGained);
            updateStatus(lastRoundNumber, estado);
        });
    }

    @Override
    public void showFinalSummary(int prize, int jokers, String endReason) {
        SwingUtilities.invokeLater(() -> {
            cancelPending("Game ended");
            bonusPanel.stopTimer();
            finalPanel.setSummary(prize, jokers, endReason);
            frame.showFinalCard();
        });
    }

    @Override
    public CompletableFuture<Boolean> requestUseJokerAsync(EstadoJogador estado, PerguntaNormalSession session) {
        return withPendingJokerDecision();
    }

    @Override
    public CompletableFuture<Integer> requestAnswerIndexAsync(PerguntaNormal pergunta, PerguntaNormalSession session,
                                                             EstadoJogador estado, int roundNumber) {
        return withPendingAnswer(pergunta, session);
    }

    @Override
    public CompletableFuture<Integer> requestBonusAnswerIndexAsync(PerguntaBonus pergunta, long remainingMs) {
        return withPendingBonusAnswer(pergunta, remainingMs);
    }

    @Override
    public CompletableFuture<Boolean> requestStopFinalRoundAsync(EstadoJogador estado) {
        return withPendingStopDecision();
    }

    private void updateStatus(int roundNumber, EstadoJogador estado) {
        int money = MoneyLevels.LEVELS[estado.getIndiceNivelDinheiro()];
        frame.updateStatus(roundNumber, money, estado.getQuantidadeJokers());
    }

    private void detectJokerElimination(PerguntaNormal pergunta, Set<Integer> remaining) {
        if (currentQuestion != pergunta) {
            currentQuestion = pergunta;
            lastRemaining = new HashSet<>(remaining);
            return;
        }
        if (lastRemaining != null && remaining.size() < lastRemaining.size()) {
            Set<Integer> removed = new HashSet<>(lastRemaining);
            removed.removeAll(remaining);
            if (!removed.isEmpty()) {
                int index = removed.iterator().next();
                char letter = (char) ('A' + index);
                frame.appendMessage("Joker used: eliminated option " + letter + ".");
            }
        }
        lastRemaining = new HashSet<>(remaining);
    }

    private void wireNormalPanelActions() {
        normalPanel.setOptionListener(index -> {
            CompletableFuture<Integer> future = pendingAnswer;
            if (future != null && !future.isDone()) {
                pendingAnswer = null;
                future.complete(index);
            }
        });
        normalPanel.setUseJokerListener(() -> {
            CompletableFuture<Boolean> future = pendingJokerDecision;
            if (future != null && !future.isDone()) {
                pendingJokerDecision = null;
                future.complete(true);
            }
        });
        normalPanel.setSkipJokerListener(() -> {
            CompletableFuture<Boolean> future = pendingJokerDecision;
            if (future != null && !future.isDone()) {
                pendingJokerDecision = null;
                future.complete(false);
            }
        });
    }

    private void wireBonusPanelActions() {
        bonusPanel.setOptionListener(index -> {
            CompletableFuture<Integer> future = pendingBonusAnswer;
            if (future != null && !future.isDone()) {
                pendingBonusAnswer = null;
                bonusPanel.stopTimer();
                correctBonusCount = bonusPanel.getCorrectCount();
                future.complete(index);
            }
        });
        bonusPanel.setTimeoutListener(() -> {
            CompletableFuture<Integer> future = pendingBonusAnswer;
            if (future != null && !future.isDone()) {
                pendingBonusAnswer = null;
                future.completeExceptionally(new TimeoutException("Bonus time expired"));
            }
        });
    }

    private CompletableFuture<Boolean> withPendingJokerDecision() {
        ensureNoPending();
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        pendingJokerDecision = future;
        SwingUtilities.invokeLater(() -> {
            normalPanel.setJokerDecisionMode(true);
            normalPanel.setJokerEnabled(true);
        });
        return future;
    }

    private CompletableFuture<Integer> withPendingAnswer(PerguntaNormal pergunta, PerguntaNormalSession session) {
        ensureNoPending();
        CompletableFuture<Integer> future = new CompletableFuture<>();
        pendingAnswer = future;
        SwingUtilities.invokeLater(() -> {
            normalPanel.setJokerDecisionMode(false);
            List<String> opcoes = pergunta.getOpcoes();
            Set<Integer> remaining = new HashSet<>(session.getOpcoesRestantes());
            normalPanel.setOptions(opcoes, remaining);
        });
        return future;
    }

    private CompletableFuture<Integer> withPendingBonusAnswer(PerguntaBonus pergunta, long remainingMs) {
        ensureNoPending();
        CompletableFuture<Integer> future = new CompletableFuture<>();
        pendingBonusAnswer = future;
        SwingUtilities.invokeLater(() -> {
            frame.showBonusCard();
            bonusPanel.setQuestion(pergunta.getEnunciado(), pergunta.getOpcoes());
            bonusPanel.startTimer(remainingMs);
        });
        return future;
    }

    private CompletableFuture<Boolean> withPendingStopDecision() {
        ensureNoPending();
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        pendingStopDecision = future;
        SwingUtilities.invokeLater(() -> {
            Object[] options = {"STOP", "Continue"};
            int choice = JOptionPane.showOptionDialog(frame,
                    "Do you want to STOP? You will lose 1 level and keep the prize.",
                    "Final Round",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[1]);
            future.complete(choice == 0);
            pendingStopDecision = null;
        });
        return future;
    }

    private void ensureNoPending() {
        if (pendingJokerDecision != null && !pendingJokerDecision.isDone()) {
            throw new IllegalStateException("Pending joker decision already exists");
        }
        if (pendingAnswer != null && !pendingAnswer.isDone()) {
            throw new IllegalStateException("Pending answer already exists");
        }
        if (pendingBonusAnswer != null && !pendingBonusAnswer.isDone()) {
            throw new IllegalStateException("Pending bonus answer already exists");
        }
        if (pendingStopDecision != null && !pendingStopDecision.isDone()) {
            throw new IllegalStateException("Pending stop decision already exists");
        }
    }

    private void cancelPending(String message) {
        if (pendingJokerDecision != null && !pendingJokerDecision.isDone()) {
            pendingJokerDecision.completeExceptionally(new IllegalStateException(message));
            pendingJokerDecision = null;
        }
        if (pendingAnswer != null && !pendingAnswer.isDone()) {
            pendingAnswer.completeExceptionally(new IllegalStateException(message));
            pendingAnswer = null;
        }
        if (pendingBonusAnswer != null && !pendingBonusAnswer.isDone()) {
            pendingBonusAnswer.completeExceptionally(new IllegalStateException(message));
            pendingBonusAnswer = null;
        }
        if (pendingStopDecision != null && !pendingStopDecision.isDone()) {
            pendingStopDecision.completeExceptionally(new IllegalStateException(message));
            pendingStopDecision = null;
        }
    }
}

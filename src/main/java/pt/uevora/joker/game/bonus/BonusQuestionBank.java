package pt.uevora.joker.game.bonus;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import pt.uevora.joker.domain.PerguntaBonus;

public class BonusQuestionBank {
    private final Deque<PerguntaBonus> perguntas;

    public BonusQuestionBank(List<PerguntaBonus> perguntas) {
        this.perguntas = new ArrayDeque<>(perguntas);
    }

    public boolean isEmpty() {
        return perguntas.isEmpty();
    }

    public PerguntaBonus next() {
        PerguntaBonus pergunta = perguntas.pollFirst();
        if (pergunta == null) {
            throw new IllegalStateException("No more bonus questions available");
        }
        return pergunta;
    }
}

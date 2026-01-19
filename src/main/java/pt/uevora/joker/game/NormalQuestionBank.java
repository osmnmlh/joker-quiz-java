package pt.uevora.joker.game;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.uevora.joker.domain.PerguntaNormal;

public class NormalQuestionBank {
    private final Map<Integer, Deque<PerguntaNormal>> perguntasPorNivel;

    public NormalQuestionBank(Map<Integer, List<PerguntaNormal>> perguntasCarregadas) {
        this.perguntasPorNivel = new HashMap<>();
        for (Map.Entry<Integer, List<PerguntaNormal>> entry : perguntasCarregadas.entrySet()) {
            this.perguntasPorNivel.put(entry.getKey(), new ArrayDeque<>(entry.getValue()));
        }
    }

    public PerguntaNormal getNextQuestionForLevel(int levelValue) {
        Deque<PerguntaNormal> fila = perguntasPorNivel.get(levelValue);
        if (fila == null) {
            throw new IllegalStateException("No questions loaded for level " + levelValue);
        }
        PerguntaNormal pergunta = fila.pollFirst();
        if (pergunta == null) {
            throw new IllegalStateException("Question bank exhausted for level " + levelValue);
        }
        return pergunta;
    }
}

package pt.uevora.joker.game.mechanics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import pt.uevora.joker.domain.PerguntaNormal;

public class PerguntaNormalSession {
    private final PerguntaNormal pergunta;
    private final Set<Integer> opcoesRestantes;
    private int jokersAplicados;

    public PerguntaNormalSession(PerguntaNormal pergunta) {
        this.pergunta = Objects.requireNonNull(pergunta, "pergunta");
        this.opcoesRestantes = new LinkedHashSet<>();
        for (int i = 0; i < pergunta.getOpcoes().size(); i++) {
            opcoesRestantes.add(i);
        }
        this.jokersAplicados = 0;
    }

    public PerguntaNormal getPergunta() {
        return pergunta;
    }

    public int getJokersAplicados() {
        return jokersAplicados;
    }

    public List<Integer> getOpcoesRestantes() {
        return Collections.unmodifiableList(new ArrayList<>(opcoesRestantes));
    }

    int getNumeroOpcoesRestantes() {
        return opcoesRestantes.size();
    }

    void removerOpcao(int indice) {
        opcoesRestantes.remove(indice);
    }

    void manterSomenteOpcao(int indice) {
        opcoesRestantes.clear();
        opcoesRestantes.add(indice);
    }

    void incrementarJokersAplicados() {
        jokersAplicados++;
    }
}

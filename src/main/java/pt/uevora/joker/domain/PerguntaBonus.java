package pt.uevora.joker.domain;

import java.util.List;

public class PerguntaBonus extends Pergunta {
    private static final long serialVersionUID = 1L;

    public PerguntaBonus(String enunciado, List<String> opcoes, int indiceCorreto) {
        super(enunciado, opcoes, indiceCorreto);
        if (opcoes.size() != 2) {
            throw new IllegalArgumentException("PerguntaBonus requires 2 options");
        }
    }
}

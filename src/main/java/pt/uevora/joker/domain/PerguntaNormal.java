package pt.uevora.joker.domain;

import java.util.List;

public class PerguntaNormal extends Pergunta {
    private static final long serialVersionUID = 1L;

    private final ElegibilidadeJoker elegibilidadeJoker;

    public PerguntaNormal(String enunciado, List<String> opcoes, int indiceCorreto,
                          ElegibilidadeJoker elegibilidadeJoker) {
        super(enunciado, opcoes, indiceCorreto);
        if (opcoes.size() != 4) {
            throw new IllegalArgumentException("PerguntaNormal requires 4 options");
        }
        if (elegibilidadeJoker != null) {
            elegibilidadeJoker.validarContraPergunta(this);
        }
        this.elegibilidadeJoker = elegibilidadeJoker;
    }

    public ElegibilidadeJoker getElegibilidadeJoker() {
        return elegibilidadeJoker;
    }
}

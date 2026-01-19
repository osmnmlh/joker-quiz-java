package pt.uevora.joker.io.parsing;

import pt.uevora.joker.domain.PerguntaNormal;

public class PerguntaNormalParseResult {
    private final int totalPerguntas;
    private final PerguntaNormal primeiraPergunta;

    public PerguntaNormalParseResult(int totalPerguntas, PerguntaNormal primeiraPergunta) {
        this.totalPerguntas = totalPerguntas;
        this.primeiraPergunta = primeiraPergunta;
    }

    public int getTotalPerguntas() {
        return totalPerguntas;
    }

    public PerguntaNormal getPrimeiraPergunta() {
        return primeiraPergunta;
    }

    public String getResumoPrimeiraPergunta() {
        if (primeiraPergunta == null) {
            return "(no questions parsed)";
        }
        return String.format("%s (opcoes=%d, correta=%d)",
                primeiraPergunta.getEnunciado(),
                primeiraPergunta.getOpcoes().size(),
                primeiraPergunta.getIndiceCorreto());
    }
}

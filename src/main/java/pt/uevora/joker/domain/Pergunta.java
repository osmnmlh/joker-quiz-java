package pt.uevora.joker.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class Pergunta implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String enunciado;
    private final List<String> opcoes;
    private final int indiceCorreto;

    protected Pergunta(String enunciado, List<String> opcoes, int indiceCorreto) {
        this.enunciado = Objects.requireNonNull(enunciado, "enunciado");
        Objects.requireNonNull(opcoes, "opcoes");
        if (opcoes.isEmpty()) {
            throw new IllegalArgumentException("opcoes must not be empty");
        }
        this.opcoes = Collections.unmodifiableList(new ArrayList<>(opcoes));
        if (indiceCorreto < 0 || indiceCorreto >= opcoes.size()) {
            throw new IllegalArgumentException("indiceCorreto out of range");
        }
        this.indiceCorreto = indiceCorreto;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public List<String> getOpcoes() {
        return opcoes;
    }

    public int getIndiceCorreto() {
        return indiceCorreto;
    }

    public boolean validarResposta(int indiceEscolhido) {
        return indiceEscolhido == indiceCorreto;
    }
}

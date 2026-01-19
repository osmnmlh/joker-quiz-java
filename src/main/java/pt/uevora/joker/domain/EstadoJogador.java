package pt.uevora.joker.domain;

import java.io.Serializable;

public class EstadoJogador implements Serializable {
    private static final long serialVersionUID = 1L;

    private int indiceNivelDinheiro;
    private int quantidadeJokers;

    public EstadoJogador(int indiceNivelDinheiro, int quantidadeJokers) {
        if (indiceNivelDinheiro < 0 || indiceNivelDinheiro > MoneyLevels.maxIndex()) {
            throw new IllegalArgumentException("indiceNivelDinheiro out of range");
        }
        if (quantidadeJokers < 0) {
            throw new IllegalArgumentException("quantidadeJokers must be >= 0");
        }
        this.indiceNivelDinheiro = indiceNivelDinheiro;
        this.quantidadeJokers = quantidadeJokers;
    }

    public int getIndiceNivelDinheiro() {
        return indiceNivelDinheiro;
    }

    public int getQuantidadeJokers() {
        return quantidadeJokers;
    }

    public void ajustarJokers(int delta) {
        quantidadeJokers = Math.max(0, quantidadeJokers + delta);
    }

    public void avancarNivelDinheiro() {
        ajustarNivelDinheiro(1);
    }

    public void descerNivelDinheiro() {
        ajustarNivelDinheiro(-1);
    }

    public void ajustarNivelDinheiro(int delta) {
        int novoIndice = indiceNivelDinheiro + delta;
        if (novoIndice < 0) {
            indiceNivelDinheiro = 0;
            return;
        }
        if (novoIndice > MoneyLevels.maxIndex()) {
            indiceNivelDinheiro = MoneyLevels.maxIndex();
            return;
        }
        indiceNivelDinheiro = novoIndice;
    }
}

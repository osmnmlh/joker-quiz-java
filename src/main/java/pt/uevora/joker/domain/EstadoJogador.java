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
        quantidadeJokers = clampJokers(quantidadeJokers + delta);
    }

    public void avancarNivelDinheiro() {
        ajustarNivelDinheiro(1);
    }

    public void descerNivelDinheiro() {
        ajustarNivelDinheiro(-1);
    }

    public void ajustarNivelDinheiro(int delta) {
        int novoIndice = indiceNivelDinheiro + delta;
        indiceNivelDinheiro = clampNivel(novoIndice);
    }

    private int clampNivel(int indice) {
        if (indice < 0) {
            return 0;
        }
        if (indice > MoneyLevels.maxIndex()) {
            return MoneyLevels.maxIndex();
        }
        return indice;
    }

    private int clampJokers(int jokers) {
        return Math.max(0, jokers);
    }
}

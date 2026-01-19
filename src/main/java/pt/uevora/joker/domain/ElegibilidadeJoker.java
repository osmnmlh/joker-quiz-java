package pt.uevora.joker.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ElegibilidadeJoker implements Serializable {
    private static final long serialVersionUID = 1L;

    private final List<Integer> indicesErradosElegiveis;

    public ElegibilidadeJoker(List<Integer> indicesErradosElegiveis) {
        Objects.requireNonNull(indicesErradosElegiveis, "indicesErradosElegiveis");
        if (indicesErradosElegiveis.size() != 2) {
            throw new IllegalArgumentException("Exactly two eligible wrong indices are required");
        }
        Set<Integer> unique = new HashSet<>(indicesErradosElegiveis);
        if (unique.size() != 2) {
            throw new IllegalArgumentException("Eligible wrong indices must be distinct");
        }
        this.indicesErradosElegiveis = Collections.unmodifiableList(new ArrayList<>(indicesErradosElegiveis));
    }

    public List<Integer> getIndicesErradosElegiveis() {
        return indicesErradosElegiveis;
    }

    void validarContraPergunta(Pergunta pergunta) {
        int totalOpcoes = pergunta.getOpcoes().size();
        int indiceCorreto = pergunta.getIndiceCorreto();
        for (Integer indice : indicesErradosElegiveis) {
            if (indice == null) {
                throw new IllegalArgumentException("Eligible wrong index cannot be null");
            }
            if (indice < 0 || indice >= totalOpcoes) {
                throw new IllegalArgumentException("Eligible wrong index out of range");
            }
            if (indice == indiceCorreto) {
                throw new IllegalArgumentException("Eligible wrong index cannot be the correct option");
            }
        }
    }
}

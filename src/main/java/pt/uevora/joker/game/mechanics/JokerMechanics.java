package pt.uevora.joker.game.mechanics;

import java.util.List;

import pt.uevora.joker.domain.ElegibilidadeJoker;
import pt.uevora.joker.domain.EstadoJogador;
import pt.uevora.joker.domain.PerguntaNormal;

public final class JokerMechanics {
    private JokerMechanics() {
    }

    public static boolean canApplyJoker(PerguntaNormalSession session, int jokersAvailable) {
        if (jokersAvailable <= 0) {
            return false;
        }
        if (session.getNumeroOpcoesRestantes() <= 1) {
            return false;
        }
        return session.getJokersAplicados() < 3;
    }

    public static void applyOneJoker(PerguntaNormalSession session, EstadoJogador playerState) {
        if (!canApplyJoker(session, playerState.getQuantidadeJokers())) {
            throw new IllegalStateException("Cannot apply joker in current state");
        }

        PerguntaNormal pergunta = session.getPergunta();
        ElegibilidadeJoker elegibilidade = pergunta.getElegibilidadeJoker();
        if (elegibilidade == null) {
            throw new IllegalStateException("Joker eligibility metadata is missing");
        }

        int indiceCorreto = pergunta.getIndiceCorreto();
        List<Integer> elegiveis = elegibilidade.getIndicesErradosElegiveis();

        Integer removida = eliminarElegivel(session, indiceCorreto, elegiveis);
        if (removida == null) {
            throw new IllegalStateException("No eligible wrong option available to eliminate");
        }

        session.removerOpcao(removida);
        System.out.println("Joker used: eliminated option " + indexToLetter(removida)
                + " (eligible wrong option).");
        session.incrementarJokersAplicados();
        playerState.ajustarJokers(-1);

        if (session.getJokersAplicados() == 3) {
            session.manterSomenteOpcao(indiceCorreto);
        }
    }

    public static void applyThreeJokersShortcut(PerguntaNormalSession session, EstadoJogador playerState) {
        if (session.getJokersAplicados() != 0) {
            throw new IllegalStateException("Shortcut requires a fresh session");
        }
        if (playerState.getQuantidadeJokers() < 3) {
            throw new IllegalStateException("Not enough jokers for shortcut");
        }

        // Scenario: correct=B (1), eligible wrong indices [0,2]
        // 1st joker removes 0, 2nd removes 2, 3rd leaves only 1.
        for (int i = 0; i < 3; i++) {
            applyOneJoker(session, playerState);
        }
    }

    private static Integer eliminarElegivel(PerguntaNormalSession session, int indiceCorreto,
                                            List<Integer> elegiveis) {
        if (elegiveis.size() < 2) {
            return null;
        }
        int a = elegiveis.get(0);
        int b = elegiveis.get(1);
        int menor = Math.min(a, b);
        int maior = Math.max(a, b);

        if (menor != indiceCorreto && session.getOpcoesRestantes().contains(menor)) {
            return menor;
        }
        if (maior != indiceCorreto && session.getOpcoesRestantes().contains(maior)) {
            return maior;
        }
        return null;
    }

    private static String indexToLetter(int index) {
        return String.valueOf((char) ('A' + index));
    }
}

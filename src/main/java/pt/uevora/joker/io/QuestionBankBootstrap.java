package pt.uevora.joker.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import pt.uevora.joker.domain.PerguntaBonus;
import pt.uevora.joker.domain.PerguntaNormal;
import pt.uevora.joker.io.parsing.PerguntaBonusParser;
import pt.uevora.joker.io.parsing.PerguntaNormalParser;

public final class QuestionBankBootstrap {
    private QuestionBankBootstrap() {
    }

    public static Map<Integer, List<PerguntaNormal>> carregarPerguntasNormais() throws IOException {
        Map<Integer, List<PerguntaNormal>> perguntasPorNivel = new LinkedHashMap<>();
        boolean todosCachesPresentes = true;
        for (int level : QuestionPaths.NORMAL_LEVELS) {
            Path cacheFile = QuestionPaths.normalCacheFile(level);
            if (!Files.exists(cacheFile)) {
                todosCachesPresentes = false;
                break;
            }
        }

        if (todosCachesPresentes) {
            for (int level : QuestionPaths.NORMAL_LEVELS) {
                perguntasPorNivel.put(level, QuestionCache.loadPerguntasNormais(level));
            }
            return perguntasPorNivel;
        }

        for (int level : QuestionPaths.NORMAL_LEVELS) {
            Path textFile = QuestionPaths.normalTextFile(level);
            if (!Files.exists(textFile)) {
                throw new IOException("Missing question file for level " + level + ": " + textFile);
            }
            List<PerguntaNormal> parsed = PerguntaNormalParser.parse(textFile);
            QuestionCache.savePerguntasNormais(level, parsed);
        }

        for (int level : QuestionPaths.NORMAL_LEVELS) {
            perguntasPorNivel.put(level, QuestionCache.loadPerguntasNormais(level));
        }
        return perguntasPorNivel;
    }

    public static List<PerguntaBonus> carregarPerguntasBonus() throws IOException {
        if (Files.exists(QuestionPaths.bonusCacheFile())) {
            return QuestionCache.loadPerguntasBonus();
        }

        Optional<Path> bonusFile = QuestionPaths.findBonusTextFile();
        if (!bonusFile.isPresent()) {
            System.out.println("Warning: no bonus question file found; skipping bonus cache.");
            return java.util.Collections.emptyList();
        }

        List<PerguntaBonus> parsed = PerguntaBonusParser.parse(bonusFile.get());
        QuestionCache.savePerguntasBonus(parsed);
        return QuestionCache.loadPerguntasBonus();
    }
}

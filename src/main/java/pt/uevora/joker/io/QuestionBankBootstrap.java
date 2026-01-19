package pt.uevora.joker.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pt.uevora.joker.domain.PerguntaNormal;
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
}

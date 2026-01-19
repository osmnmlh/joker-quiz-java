package pt.uevora.joker.io;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import pt.uevora.joker.domain.PerguntaBonus;
import pt.uevora.joker.domain.PerguntaNormal;

public final class QuestionCache {
    private QuestionCache() {
    }

    public static void savePerguntasNormais(int levelValue, List<PerguntaNormal> perguntas) throws IOException {
        ensureCacheDir();
        Path cacheFile = QuestionPaths.normalCacheFile(levelValue);
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(cacheFile))) {
            out.writeObject(perguntas);
        }
    }

    public static List<PerguntaNormal> loadPerguntasNormais(int levelValue) throws IOException {
        Path cacheFile = QuestionPaths.normalCacheFile(levelValue);
        Object obj = readObject(cacheFile, "normal questions", levelValue);
        return castPerguntasNormais(obj, cacheFile);
    }

    public static void savePerguntasBonus(List<PerguntaBonus> perguntas) throws IOException {
        ensureCacheDir();
        Path cacheFile = QuestionPaths.bonusCacheFile();
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(cacheFile))) {
            out.writeObject(perguntas);
        }
    }

    public static List<PerguntaBonus> loadPerguntasBonus() throws IOException {
        Path cacheFile = QuestionPaths.bonusCacheFile();
        Object obj = readObject(cacheFile, "bonus questions", null);
        return castPerguntasBonus(obj, cacheFile);
    }

    private static void ensureCacheDir() throws IOException {
        Files.createDirectories(QuestionPaths.CACHE_DIR);
    }

    private static Object readObject(Path cacheFile, String label, Integer level) throws IOException {
        if (!Files.exists(cacheFile)) {
            String suffix = level == null ? "" : " for level " + level;
            throw new IOException("Cache file not found for " + label + suffix + ": " + cacheFile);
        }
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(cacheFile))) {
            return in.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException("Cache file contains unknown class: " + cacheFile, e);
        }
    }

    private static List<PerguntaNormal> castPerguntasNormais(Object obj, Path cacheFile) throws IOException {
        if (obj == null) {
            throw new IOException("Cache file is empty: " + cacheFile);
        }
        if (!(obj instanceof List)) {
            throw new IOException("Cache file does not contain a List: " + cacheFile);
        }
        List<?> rawList = (List<?>) obj;
        List<PerguntaNormal> perguntas = new ArrayList<>();
        for (Object item : rawList) {
            if (item == null) {
                throw new IOException("Cache file contains null normal question entry: " + cacheFile);
            }
            if (!(item instanceof PerguntaNormal)) {
                throw new IOException("Cache file contains invalid normal question entry: " + cacheFile);
            }
            perguntas.add((PerguntaNormal) item);
        }
        return perguntas;
    }

    private static List<PerguntaBonus> castPerguntasBonus(Object obj, Path cacheFile) throws IOException {
        if (obj == null) {
            throw new IOException("Cache file is empty: " + cacheFile);
        }
        if (!(obj instanceof List)) {
            throw new IOException("Cache file does not contain a List: " + cacheFile);
        }
        List<?> rawList = (List<?>) obj;
        List<PerguntaBonus> perguntas = new ArrayList<>();
        for (Object item : rawList) {
            if (item == null) {
                throw new IOException("Cache file contains null bonus question entry: " + cacheFile);
            }
            if (!(item instanceof PerguntaBonus)) {
                throw new IOException("Cache file contains invalid bonus question entry: " + cacheFile);
            }
            perguntas.add((PerguntaBonus) item);
        }
        return perguntas;
    }
}

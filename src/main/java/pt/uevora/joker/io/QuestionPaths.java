package pt.uevora.joker.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class QuestionPaths {
    public static final int[] NORMAL_LEVELS = {200, 500, 1000, 3000, 10000, 50000};
    // Cache layout: repo-relative directory "data/cache/" with one .ser per level + bonus.
    public static final Path CACHE_DIR = Paths.get("data", "cache");
    public static final String BONUS_CACHE_FILE = "perguntas_bonus.ser";

    private QuestionPaths() {
    }

    public static Path normalTextFile(int levelValue) {
        return Paths.get(String.format("perguntas_%d.txt", levelValue));
    }

    public static Path normalCacheFile(int levelValue) {
        return CACHE_DIR.resolve(String.format("perguntas_%d.ser", levelValue));
    }

    public static Path bonusCacheFile() {
        return CACHE_DIR.resolve(BONUS_CACHE_FILE);
    }

    public static Optional<Path> findBonusTextFile() throws IOException {
        try (Stream<Path> stream = Files.list(Paths.get("."))) {
            List<Path> matches = stream
                    .filter(path -> Files.isRegularFile(path))
                    .filter(path -> path.getFileName().toString().toLowerCase().contains("bonus"))
                    .filter(path -> path.getFileName().toString().toLowerCase().endsWith(".txt"))
                    .sorted(Comparator.comparing(path -> path.getFileName().toString()))
                    .collect(Collectors.toList());
            if (matches.isEmpty()) {
                return Optional.empty();
            }
            if (matches.size() > 1) {
                System.out.println("Warning: multiple bonus files found; using " + matches.get(0).getFileName());
            }
            return Optional.of(matches.get(0));
        }
    }
}

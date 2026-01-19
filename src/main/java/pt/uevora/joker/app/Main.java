package pt.uevora.joker.app;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import pt.uevora.joker.domain.PerguntaNormal;
import pt.uevora.joker.game.JogoDoJoker;
import pt.uevora.joker.io.QuestionCache;
import pt.uevora.joker.io.QuestionPaths;
import pt.uevora.joker.io.parsing.PerguntaNormalParser;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            return;
        }

        String mode = args[0].trim().toLowerCase();
        try {
            switch (mode) {
                case "build-cache":
                    handleBuildCache();
                    break;
                case "play":
                    new JogoDoJoker().jogar();
                    break;
                default:
                    printUsage();
                    break;
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void handleBuildCache() throws IOException {
        for (int level : QuestionPaths.NORMAL_LEVELS) {
            Path textFile = QuestionPaths.normalTextFile(level);
            if (!Files.exists(textFile)) {
                throw new IOException("Missing question file for level " + level + ": " + textFile);
            }
            List<PerguntaNormal> perguntas = PerguntaNormalParser.parse(textFile);
            QuestionCache.savePerguntasNormais(level, perguntas);
            System.out.println("Cached " + perguntas.size() + " questions for level " + level);
        }

        Optional<Path> bonusFile = QuestionPaths.findBonusTextFile();
        if (bonusFile.isPresent()) {
            System.out.println("Warning: bonus file found at " + bonusFile.get()
                    + ", but bonus parsing is not implemented yet. Skipping bonus cache.");
        } else {
            System.out.println("Warning: no bonus question file found; skipping bonus cache.");
        }
    }

    private static void printUsage() {
        System.out.println("Usage:");
        System.out.println("  java -jar <app>.jar build-cache");
        System.out.println("  java -jar <app>.jar play");
    }
}

package pt.uevora.joker.io.parsing;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import pt.uevora.joker.domain.ElegibilidadeJoker;
import pt.uevora.joker.domain.PerguntaNormal;

public final class PerguntaNormalParser {
    private PerguntaNormalParser() {
    }

    public static List<PerguntaNormal> parse(Path path) throws IOException {
        List<PerguntaNormal> perguntas = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            int lineNumber = 0;
            while (true) {
                String enunciado = null;
                while ((line = reader.readLine()) != null) {
                    lineNumber++;
                    if (!line.trim().isEmpty()) {
                        enunciado = line;
                        break;
                    }
                }
                if (enunciado == null) {
                    break;
                }
                String[] optionLines = new String[4];
                for (int i = 0; i < 4; i++) {
                    line = reader.readLine();
                    lineNumber++;
                    if (line == null) {
                        throw new IllegalArgumentException("Unexpected end of file while reading options at line "
                                + lineNumber);
                    }
                    optionLines[i] = line;
                }
                String correctLine = reader.readLine();
                lineNumber++;
                if (correctLine == null) {
                    throw new IllegalArgumentException("Unexpected end of file while reading correct option at line "
                            + lineNumber);
                }
                String separator = reader.readLine();
                lineNumber++;
                if (separator != null && !separator.trim().isEmpty()) {
                    throw new IllegalArgumentException("Expected blank separator line at line " + lineNumber);
                }

                PerguntaNormal pergunta = buildPergunta(enunciado, optionLines, correctLine, lineNumber - 1);
                perguntas.add(pergunta);

                if (separator == null) {
                    break;
                }
            }
        }
        return perguntas;
    }

    public static PerguntaNormalParseResult verificarArquivo(Path path) throws IOException {
        List<PerguntaNormal> perguntas = parse(path);
        PerguntaNormal primeira = perguntas.isEmpty() ? null : perguntas.get(0);
        return new PerguntaNormalParseResult(perguntas.size(), primeira);
    }

    private static PerguntaNormal buildPergunta(String enunciado, String[] optionLines, String correctLine,
                                                int correctLineNumber) {
        String enunciadoTrim = enunciado.trim();
        if (enunciadoTrim.isEmpty()) {
            throw new IllegalArgumentException("Question text is empty at line " + (correctLineNumber - 5));
        }

        List<String> opcoes = new ArrayList<>();
        String[] labels = {"A.", "B.", "C.", "D."};
        for (int i = 0; i < optionLines.length; i++) {
            String linhaOpcao = optionLines[i];
            if (linhaOpcao == null) {
                throw new IllegalArgumentException("Missing option line for label " + labels[i]);
            }
            String trimmed = linhaOpcao.trim();
            if (!trimmed.startsWith(labels[i])) {
                throw new IllegalArgumentException("Expected option line starting with " + labels[i]
                        + " at line " + (correctLineNumber - 4 + i));
            }
            String opcao = trimmed.substring(labels[i].length()).trim();
            if (opcao.isEmpty()) {
                throw new IllegalArgumentException("Option text is empty for label " + labels[i]
                        + " at line " + (correctLineNumber - 4 + i));
            }
            opcoes.add(opcao);
        }

        String correctTrim = correctLine.trim();
        int indiceCorreto = letterToIndex(correctTrim, correctLineNumber);

        // Deterministic fallback due to missing metadata in provided files.
        ElegibilidadeJoker elegibilidade = new ElegibilidadeJoker(deterministicEligibleWrongIndices(indiceCorreto));
        return new PerguntaNormal(enunciadoTrim, opcoes, indiceCorreto, elegibilidade);
    }

    private static int letterToIndex(String letter, int lineNumber) {
        if (letter.length() != 1) {
            throw new IllegalArgumentException("Correct option must be a single letter at line " + lineNumber);
        }
        switch (letter) {
            case "A":
                return 0;
            case "B":
                return 1;
            case "C":
                return 2;
            case "D":
                return 3;
            default:
                throw new IllegalArgumentException("Correct option must be A/B/C/D at line " + lineNumber);
        }
    }

    private static List<Integer> deterministicEligibleWrongIndices(int indiceCorreto) {
        List<Integer> indices = new ArrayList<>(2);
        for (int i = 0; i < 4; i++) {
            if (i != indiceCorreto) {
                indices.add(i);
            }
            if (indices.size() == 2) {
                break;
            }
        }
        return indices;
    }
}

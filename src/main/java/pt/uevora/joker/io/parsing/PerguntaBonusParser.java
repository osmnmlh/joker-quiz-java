package pt.uevora.joker.io.parsing;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import pt.uevora.joker.domain.PerguntaBonus;

public final class PerguntaBonusParser {
    private PerguntaBonusParser() {
    }

    public static List<PerguntaBonus> parse(Path file) throws IOException {
        List<PerguntaBonus> perguntas = new ArrayList<>();
        String fileName = file.toString();
        try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String line;
            int blockNumber = 0;
            while (true) {
                String enunciado = null;
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        enunciado = line;
                        break;
                    }
                }
                if (enunciado == null) {
                    break;
                }
                blockNumber++;
                String enunciadoTrim = enunciado.trim();
                if (enunciadoTrim.isEmpty()) {
                    throw new IllegalArgumentException(formatError(fileName, blockNumber,
                            "non-empty question text", "empty line"));
                }

                String lineA = reader.readLine();
                if (lineA == null) {
                    throw new IllegalArgumentException(formatError(fileName, blockNumber,
                            "option line starting with 'A.'", "EOF"));
                }
                if (!lineA.startsWith("A.")) {
                    throw new IllegalArgumentException(formatError(fileName, blockNumber,
                            "option line starting with 'A.'", lineA));
                }
                String opcaoA = lineA.substring(2).trim();
                if (opcaoA.isEmpty()) {
                    throw new IllegalArgumentException(formatError(fileName, blockNumber,
                            "non-empty option A", lineA));
                }

                String lineB = reader.readLine();
                if (lineB == null) {
                    throw new IllegalArgumentException(formatError(fileName, blockNumber,
                            "option line starting with 'B.'", "EOF"));
                }
                if (!lineB.startsWith("B.")) {
                    throw new IllegalArgumentException(formatError(fileName, blockNumber,
                            "option line starting with 'B.'", lineB));
                }
                String opcaoB = lineB.substring(2).trim();
                if (opcaoB.isEmpty()) {
                    throw new IllegalArgumentException(formatError(fileName, blockNumber,
                            "non-empty option B", lineB));
                }

                String respostaLine = reader.readLine();
                if (respostaLine == null) {
                    throw new IllegalArgumentException(formatError(fileName, blockNumber,
                            "answer line starting with 'Resposta:'", "EOF"));
                }
                if (!respostaLine.startsWith("Resposta:")) {
                    throw new IllegalArgumentException(formatError(fileName, blockNumber,
                            "answer line starting with 'Resposta:'", respostaLine));
                }
                String resposta = respostaLine.substring("Resposta:".length()).trim();
                int indiceCorreto = mapResposta(fileName, blockNumber, resposta);

                String separator = reader.readLine();
                if (separator != null && !separator.trim().isEmpty()) {
                    throw new IllegalArgumentException(formatError(fileName, blockNumber,
                            "blank line separator", separator));
                }

                List<String> opcoes = new ArrayList<>();
                opcoes.add(opcaoA);
                opcoes.add(opcaoB);
                perguntas.add(new PerguntaBonus(enunciadoTrim, opcoes, indiceCorreto));

                if (separator == null) {
                    break;
                }
            }
        }
        return perguntas;
    }

    private static int mapResposta(String fileName, int blockNumber, String resposta) {
        if ("A".equals(resposta)) {
            return 0;
        }
        if ("B".equals(resposta)) {
            return 1;
        }
        throw new IllegalArgumentException(formatError(fileName, blockNumber,
                "answer letter 'A' or 'B'", resposta));
    }

    private static String formatError(String fileName, int blockNumber, String expected, String found) {
        return String.format("File %s, block %d: expected %s but found '%s'", fileName, blockNumber, expected, found);
    }
}

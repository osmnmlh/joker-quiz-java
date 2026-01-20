# Joker Quiz (Java)

Java OOP simulation of “Jogo do Joker”. This repository includes parsing, caching, the 12-round core loop, and bonus rounds.

## TL;DR Quick Run

```bash
mvn -q -DskipTests package
java -cp target/joker-quiz-0.1.0-SNAPSHOT.jar pt.uevora.joker.app.Main build-cache
java -cp target/joker-quiz-0.1.0-SNAPSHOT.jar pt.uevora.joker.app.Main play
```

Notes:
- Place question text files in the repo root (`perguntas_*.txt`, plus `perguntas_bonus.txt` if provided).
- Cache files are written to `data/cache/`.

## Build

```bash
mvn -q -DskipTests package
```

Output artifact:
- `target/joker-quiz-0.1.0-SNAPSHOT.jar`

## Run (CLI)

### Build caches (normal questions, bonus if file exists)

```bash
java -cp target/joker-quiz-0.1.0-SNAPSHOT.jar pt.uevora.joker.app.Main build-cache
```

Expected signals:
- `Cached <N> questions for level <level>` per normal level.
- `Warning: no bonus question file found; skipping bonus cache.` if bonus file is missing.

### Play

```bash
java -cp target/joker-quiz-0.1.0-SNAPSHOT.jar pt.uevora.joker.app.Main play
```

Expected signals:
- Normal round prompts for joker use and answer selection.
- Bonus rounds trigger after rounds 4 and 8 (if bonus cache exists), otherwise a warning is printed and the bonus is skipped.

## Question Files & Cache Locations

- **Normal question files** must be in the repo root:
  - `perguntas_200.txt`, `perguntas_500.txt`, `perguntas_1000.txt`, `perguntas_3000.txt`, `perguntas_10000.txt`, `perguntas_50000.txt`
- **Bonus question file** (if present) must be in the repo root and include `bonus` in its filename (e.g., `perguntas_bonus.txt`).
- **Cache output** is written to `data/cache/`:
  - `perguntas_200.ser`, `perguntas_500.ser`, `perguntas_1000.ser`, `perguntas_3000.ser`, `perguntas_10000.ser`, `perguntas_50000.ser`, `perguntas_bonus.ser`

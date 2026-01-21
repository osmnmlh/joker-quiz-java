# Runbook

## Build

```bash
mvn -q -DskipTests package
```

Expected output:
- JAR at `target/joker-quiz-0.1.0-SNAPSHOT.jar`.

## Run

### Build cache

```bash
java -cp target/classes pt.uevora.joker.app.Main build-cache
```

Alternate (after packaging a JAR):

```bash
java -cp target/joker-quiz-0.1.0-SNAPSHOT.jar pt.uevora.joker.app.Main build-cache
```

Expected output signals:
- `Cached <N> questions for level <level>` for each normal level.
- `Warning: no bonus question file found; skipping bonus cache.` if bonus file is missing.

### Play

```bash
java -cp target/joker-quiz-0.1.0-SNAPSHOT.jar pt.uevora.joker.app.Main play
```

Expected output signals:
- Prompts to use jokers and answer questions.
- Bonus rounds after rounds 4 and 8 if bonus cache exists; otherwise a warning that bonus questions are missing.

### Run GUI (optional)

```bash
java -cp target/classes pt.uevora.joker.app.GuiMain
```

Notes:
- If the environment is headless, the GUI will refuse to launch.
- This GUI uses Swing to avoid JavaFX environment issues.

## Question Files & Cache Paths

- Normal question files are expected in the repo root:
  - `perguntas_200.txt`, `perguntas_500.txt`, `perguntas_1000.txt`, `perguntas_3000.txt`, `perguntas_10000.txt`, `perguntas_50000.txt`
- Bonus question file (if present) must be in the repo root and include `bonus` in its filename.
- Cache files are written to `data/cache/`:
  - `perguntas_200.ser`, `perguntas_500.ser`, `perguntas_1000.ser`, `perguntas_3000.ser`, `perguntas_10000.ser`, `perguntas_50000.ser`, `perguntas_bonus.ser`

## Tests

No automated tests are configured in this repository.

## Troubleshooting

- **Cache errors**: delete `data/cache/` and re-run `build-cache` to regenerate `.ser` files.
- **Missing bonus file**: the game will warn and skip bonus rounds; place the bonus `.txt` file in the repo root and re-run `build-cache`.

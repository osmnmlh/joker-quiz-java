# Manual Test Scripts

> All commands assume you are in the repo root. For deterministic results, inspect the question files to know correct answers before running tests. Do **not** edit the files; only read them.

## MT-01 Build cache from scratch (parse-once baseline)
- **Setup**: Delete cache directory if it exists.
  - `rm -rf data/cache`
- **Command**:
  - `java -cp target/joker-quiz-0.1.0-SNAPSHOT.jar pt.uevora.joker.app.Main build-cache`
- **Input**: None.
- **Expected**:
  - Lines like `Cached <N> questions for level 200` for each normal level.
  - If no bonus file: `Warning: no bonus question file found; skipping bonus cache.`
  - New files created in `data/cache/`.

## MT-02 Play from cache (no re-parse)
- **Setup**: Ensure `data/cache/` exists from MT-01.
- **Command**:
  - `java -cp target/joker-quiz-0.1.0-SNAPSHOT.jar pt.uevora.joker.app.Main play`
- **Input**: Provide any valid inputs for prompts.
- **Expected**:
  - No `Cached ...` lines (those only appear in build-cache).
  - Gameplay begins immediately, indicating cached data is used.

## MT-03 Correct answer advances money level
- **Setup**: Use `perguntas_200.txt` to identify the correct answer for the first question.
- **Command**: `java -cp target/joker-quiz-0.1.0-SNAPSHOT.jar pt.uevora.joker.app.Main play`
- **Input**:
  - When asked to use joker: `n`
  - Answer with the correct option letter for the first question.
- **Expected**:
  - Output: `Correct! Moving up a level.`
  - Money level index increments by 1.

## MT-04 Wrong answer with jokers >= 3 (lose 3 jokers)
- **Setup**: Start a fresh game (initial jokers = 7).
- **Command**: `java -cp target/joker-quiz-0.1.0-SNAPSHOT.jar pt.uevora.joker.app.Main play`
- **Input**:
  - Use joker: `n`
  - Answer with a known wrong option.
- **Expected**:
  - Output: `Wrong answer.`
  - Jokers decrease by 3, money level unchanged.

## MT-05 Wrong answer with jokers N < 3 (level drop)
- **Setup**: Spend jokers until you have 2 or fewer (e.g., use jokers on multiple questions).
- **Command**: `java -cp target/joker-quiz-0.1.0-SNAPSHOT.jar pt.uevora.joker.app.Main play`
- **Input**:
  - Ensure current joker count is N < 3.
  - Answer a question incorrectly.
- **Expected**:
  - Money level index decreases by `(3 - N)` and clamps to 0 if needed.
  - Jokers drop to 0 (current policy in `JogoDoJoker`).

## MT-06 Joker usage 1 → 2 → 3 on a single question
- **Setup**: Identify a question with known correct answer; start a new question.
- **Command**: `java -cp target/joker-quiz-0.1.0-SNAPSHOT.jar pt.uevora.joker.app.Main play`
- **Input**:
  - Use joker: `y` (three times)
- **Expected**:
  - After each joker, one wrong option disappears.
  - After the third joker, only the correct option remains.

## MT-07 Wrong after spending 2 jokers (total loss scenario)
- **Setup**: On a question, use two jokers to remove two wrong options.
- **Command**: `java -cp target/joker-quiz-0.1.0-SNAPSHOT.jar pt.uevora.joker.app.Main play`
- **Input**:
  - Use joker: `y` twice
  - Answer incorrectly
- **Expected**:
  - Two jokers were already spent.
  - Wrong answer applies penalty (lose 3 jokers if still >=3), demonstrating the total loss can reach 5.

## MT-08 No repeated questions + exhaustion fail-fast
- **Setup**: Use a small question file or repeatedly answer to exhaust a level.
- **Command**: `java -cp target/joker-quiz-0.1.0-SNAPSHOT.jar pt.uevora.joker.app.Main play`
- **Input**: Continue answering to consume all questions for a level.
- **Expected**:
  - When questions run out, the game ends with an error message:
    - `Question bank exhausted for level <level>`

## MT-09 Bonus round triggers after round 4
- **Setup**: Bonus cache exists (`data/cache/perguntas_bonus.ser`).
- **Command**: `java -cp target/joker-quiz-0.1.0-SNAPSHOT.jar pt.uevora.joker.app.Main play`
- **Input**: Play through 4 rounds.
- **Expected**:
  - `=== Bonus Round (60 seconds) ===` appears after round 4.

## MT-10 Bonus reward calculation (+1 joker per 5 correct)
- **Setup**: Bonus cache exists and you know 5 correct answers.
- **Command**: `java -cp target/joker-quiz-0.1.0-SNAPSHOT.jar pt.uevora.joker.app.Main play`
- **Input**: Answer 5 bonus questions correctly within the time limit.
- **Expected**:
  - `Bonus correct answers: 5`
  - `Jokers awarded: 1`
  - Player joker count increases by 1 after the bonus ends.

## MT-11 Bonus timeout behavior
- **Setup**: Bonus cache exists.
- **Command**: `java -cp target/joker-quiz-0.1.0-SNAPSHOT.jar pt.uevora.joker.app.Main play`
- **Input**: Start bonus round and wait for 60 seconds without answering.
- **Expected**:
  - `Time is up!`
  - Bonus ends and awards jokers based on correct count so far.

## MT-12 Last round STOP rule
- **Setup**: Reach round 12.
- **Command**: `java -cp target/joker-quiz-0.1.0-SNAPSHOT.jar pt.uevora.joker.app.Main play`
- **Input**: When prompted in the final round, enter `y` to stop.
- **Expected**:
  - Money level decreases by 1 (clamped to 0 if necessary).
  - Game ends and prints final prize and joker count.

## MT-13 Input validation (reprompt)
- **Setup**: Start a new game.
- **Command**: `java -cp target/joker-quiz-0.1.0-SNAPSHOT.jar pt.uevora.joker.app.Main play`
- **Input**:
  - Enter invalid responses for stop/joker prompts (e.g., `maybe`, `123`).
  - Enter invalid answer options (e.g., `E`, `1`).
- **Expected**:
  - The program re-prompts with guidance until valid input is provided.

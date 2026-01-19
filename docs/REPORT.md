# Report — Jogo do Joker (Java)

## Problem Statement

This project implements a Java OOP simulation of the game “Jogo do Joker.” The system reads normal (4-option) questions from text files, builds serialized caches, runs a 12-round game with joker mechanics and penalties, and triggers timed bonus rounds after rounds 4 and 8.

## Architecture Overview

**Packages**
- `pt.uevora.joker.domain`: core models (`Pergunta`, `PerguntaNormal`, `PerguntaBonus`, `EstadoJogador`, `MoneyLevels`, `ElegibilidadeJoker`).
- `pt.uevora.joker.io`: cache/serialization (`QuestionCache`, `QuestionBankBootstrap`, `QuestionPaths`).
- `pt.uevora.joker.io.parsing`: text parsing for normal questions (`PerguntaNormalParser`).
- `pt.uevora.joker.game`: core gameplay (`JogoDoJoker`, `NormalQuestionBank`).
- `pt.uevora.joker.game.mechanics`: joker elimination logic (`JokerMechanics`, `PerguntaNormalSession`).
- `pt.uevora.joker.game.bonus`: bonus round flow (`BonusRound`, `BonusQuestionBank`).
- `pt.uevora.joker.app`: CLI entry (`Main`).

## Data Flow

1. **Text files → Objects**: `PerguntaNormalParser` loads UTF-8 text questions into `PerguntaNormal` objects.
2. **Objects → Cache**: `QuestionCache` serializes lists to `.ser` files in `data/cache/`.
3. **Cache → Gameplay**: `QuestionBankBootstrap` loads serialized caches for normal rounds; gameplay uses `NormalQuestionBank` to avoid repeats.
4. **Bonus**: bonus cache is loaded if present; otherwise bonus is skipped safely.

## Key Algorithms / Rules

- **Money progression & penalties**:
  - Correct answers advance one money level.
  - Wrong answers: if jokers ≥ 3, lose 3 jokers; if jokers < 3, drop money by `(3 - N)` levels (clamped at 0) and set jokers to 0 (current policy).
- **Joker elimination**:
  - Each joker removes one wrong option from the two eligible wrong indices.
  - Third joker leaves only the correct option.
- **Bonus rounds**:
  - Trigger after rounds 4 and 8, last 60 seconds.
  - Bonus questions have 2 options.
  - Reward: `floor(correct / 5)` jokers added after the bonus ends.
- **No repeat questions**:
  - Normal questions are dequeued per level; exhaustion fails fast with a clear error.

## Limitations / Assumptions

- Bonus text file may be absent; in that case, bonus rounds are skipped with warnings.
- Bonus input timing is best-effort in a console: a question in progress may finish after timeout, but no new question starts after timeout.

## How to Run & Verify

- Build and run instructions are in `docs/RUNBOOK.md`.
- Deterministic manual tests are in `docs/MANUAL_TESTS.md`.
- Compliance mapping is in `docs/SPEC_COMPLIANCE.md`.

## Compliance Mapping Note

Every requirement from the spec is mapped to evidence in `docs/SPEC_COMPLIANCE.md`.

# Defense Script & Demo Plan

## 3-Minute Explanation Script (Bullet Guide)

- **Architecture**: explain packages and responsibilities (`domain`, `io`, `game`, `game.mechanics`, `game.bonus`, `app`).
- **Persistence pipeline**: text files → parsed questions → serialized caches in `data/cache/` → gameplay reads `.ser` files.
- **Joker mechanics proof**: two eligible wrong indices are stored in `ElegibilidadeJoker`; a joker removes only an eligible wrong option (see `JokerMechanics`).
- **Penalty math**: wrong answers lose 3 jokers if possible; otherwise drop money levels by `(3 − N)` and clamp.
- **Bonus timing proof**: bonus starts after rounds 4 and 8, lasts 60 seconds, and awards `floor(correct/5)` jokers.
- **No repeats**: `NormalQuestionBank` dequeues per level and fails fast when exhausted.

## 5-Minute Live Demo Plan

1. **Show repo root question files**
   - Point out `perguntas_200.txt` ... `perguntas_50000.txt` and `perguntas_bonus.txt`.
2. **Build cache**
   - Run: `java -cp target/joker-quiz-0.1.0-SNAPSHOT.jar pt.uevora.joker.app.Main build-cache`
   - Show cache creation lines under `data/cache/`.
3. **Start play**
   - Run: `java -cp target/joker-quiz-0.1.0-SNAPSHOT.jar pt.uevora.joker.app.Main play`
   - Show first question prompt.
4. **Demonstrate joker elimination**
   - Use one joker and highlight the eliminated option and remaining choices.
5. **Describe bonus**
   - If time allows, reach round 4; otherwise explain that it triggers after round 4 and lasts 60 seconds.
6. **Parse-once proof (optional live)**
   - Explain the proof: after cache exists, rename `.txt` files and show that play still runs from cache.

## Likely Professor Questions (Crisp Answers)

**Q: How do you ensure text files are parsed only once?**
- `QuestionBankBootstrap` checks for cache files; if present, it loads `.ser` files instead of parsing.

**Q: How do you prove no repeated questions?**
- `NormalQuestionBank` dequeues per level; once empty it throws, showing repeats cannot occur.

**Q: How are eligible wrong options enforced for jokers?**
- `ElegibilidadeJoker` stores the two eligible wrong indices, and `JokerMechanics#eliminarElegivel` removes only from those indices.

**Q: How is the penalty when N < 3 jokers implemented?**
- `JogoDoJoker#aplicarPenalidadePorErro` applies `K1 = K − (3 − N)` and clamps via `EstadoJogador`.

**Q: How do bonus rounds work?**
- `BonusRound` runs a 60-second loop, accepts A/B answers, and awards `floor(correct / 5)` jokers.

# Defense Script & Demo Plan

## 2–3 Minute Explanation Script (Bullet Guide)

- We implemented a Java OOP simulation of “Jogo do Joker.”
- The system loads normal questions from text files, serializes them, and runs a 12-round game.
- We enforce no repeated questions by dequeuing per level; if exhausted, the game fails fast.
- Joker mechanics remove wrong options deterministically based on two eligible indices.
- Wrong-answer penalties follow the spec: lose 3 jokers if you have at least 3, otherwise drop money levels by `(3 - N)`.
- Bonus rounds occur after rounds 4 and 8, last 60 seconds, and award +1 joker per 5 correct.
- Caching ensures text files are parsed once; subsequent runs load from `.ser` files.

## 2-Minute Live Demo Plan

1. **Build cache**
   - Run: `java -cp target/joker-quiz-0.1.0-SNAPSHOT.jar pt.uevora.joker.app.Main build-cache`
   - Show cache creation lines.
2. **Play start**
   - Run: `java -cp target/joker-quiz-0.1.0-SNAPSHOT.jar pt.uevora.joker.app.Main play`
   - Show the first question prompt.
3. **Joker usage**
   - Use a joker and show one option disappears.
4. **Bonus trigger**
   - Reach round 4 (or explain quickly if time is short): show bonus round screen and timing.
5. **Stop rule**
   - Describe the round 12 stop rule if reaching it live is not feasible.

## Likely Questions & Short Answers

**Q: Why serialization?**
- To ensure text files are parsed only once and subsequent runs read cached object files, matching the assignment requirement.

**Q: How do you ensure “parse once”?**
- `QuestionBankBootstrap` checks for cache files and parses only if caches are missing; play mode loads from cache.

**Q: How do you ensure “no repeated questions”?**
- `NormalQuestionBank` dequeues questions per level and throws if exhausted.

**Q: How do you guarantee the joker removes only eligible wrong options?**
- `ElegibilidadeJoker` stores the two eligible wrong indices and `JokerMechanics#eliminarElegivel` selects only from them.

**Q: How is the penalty formula implemented?**
- `JogoDoJoker#aplicarPenalidadePorErro` applies the `(3 − N)` level drop and clamps via `EstadoJogador`.

**Q: How do you enforce the 60-second bonus?**
- `BonusRound` uses a timed loop with a `Future.get(timeout)`; it won’t start a new question after timeout.

**Q: How do you implement “two possible wrong options”?**
- `ElegibilidadeJoker` stores the two eligible wrong indices, and `JokerMechanics` removes the lowest eligible still present.

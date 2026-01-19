# Spec Compliance Map

## Requirements Checklist

- REQ-1 Implement the project in Java using OOP/POO. [done]
  Evidence: Java classes under src/main/java/pt/uevora/joker/**. 
- REQ-2 Build an application that simulates “Jogo do Joker”. [done]
  Evidence: Core loop in pt.uevora.joker.game.JogoDoJoker.jogar().
- REQ-3 Implement money level logic, joker management, and question file handling. [done]
  Evidence: MoneyLevels, EstadoJogador, JokerMechanics, parsing/cache modules.
- REQ-4 Assignment is split into 3 parts; Part I is mandatory. [not yet]
  Evidence: N/A (admin/process).
- REQ-5 Read whole statement even if not doing all parts. [not yet]
  Evidence: N/A (admin/process).

- REQ-6 Game has 12 rounds. [done]
  Evidence: JogoDoJoker TOTAL_ROUNDS = 12.
- REQ-7 Objective: max prize 50000€. [done]
  Evidence: MoneyLevels includes 50000 as max value.
- REQ-8 Two towers: Jokers and money. [done]
  Evidence: EstadoJogador tracks money level index and jokers.
- REQ-9 Start at money level 0 with 7 jokers. [done]
  Evidence: JogoDoJoker START_LEVEL_INDEX=0, START_JOKERS=7.
- REQ-10 Each normal round: new question with 4 options. [done]
  Evidence: PerguntaNormal (4 options) used in JogoDoJoker rounds.
- REQ-11 Correct answer: advance one money level. [done]
  Evidence: JogoDoJoker calls EstadoJogador.avancarNivelDinheiro().
- REQ-12 Wrong + jokers >=3: lose 3 jokers, money level unchanged. [done]
  Evidence: JogoDoJoker.aplicarPenalidadePorErro() branch jokers>=3.
- REQ-13 Wrong + jokers N<3: descend money level with K1 + (3−N) = K. [done]
  Evidence: JogoDoJoker.aplicarPenalidadePorErro() computes new index with (3 - jokers).
- REQ-14 Player may use jokers to reduce options. [done]
  Evidence: JokerMechanics and JogoDoJoker joker prompts.
- REQ-15 Using 1 joker: lose 1 joker and eliminate an incorrect option among two possible incorrect options. [done]
  Evidence: JokerMechanics.applyOneJoker() uses ElegibilidadeJoker indices and decrements jokers.
- REQ-16 Note: using 3 jokers leaves only correct option; lose 3 jokers. [done]
  Evidence: JokerMechanics.applyOneJoker() keeps only correct option after 3rd use.
- REQ-17 Note: using 2 jokers then wrong can lose 5 jokers total. [done]
  Evidence: JokerMechanics + wrong-answer penalty (3 jokers) can total 5; see JokerMechanics + JogoDoJoker.aplicarPenalidadePorErro().
- REQ-18 Final prize equals final money level reached. [done]
  Evidence: JogoDoJoker prints final prize from MoneyLevels using player index.
- REQ-19 Bonus rounds at end of round #4 and round #8. [done]
  Evidence: JogoDoJoker triggers bonus when round == 4 or 8.
- REQ-20 Bonus: answer as many as possible in 1 minute. [done]
  Evidence: BonusRound enforces 60-second window.
- REQ-21 Bonus questions have 2 options. [done]
  Evidence: PerguntaBonus requires 2 options; BonusRound prints A/B options.
- REQ-22 Bonus reward: +1 joker per 5 correct. [done]
  Evidence: BonusRound awards corretas / 5 jokers.
- REQ-23 Last round: player may stop and descend one money level. [done]
  Evidence: JogoDoJoker asks to stop in final round and adjusts level down by 1.
- REQ-24 Advice note about stopping in last round (no implementation required). [not yet]
  Evidence: N/A (admin/process).

- REQ-25 Questions provided in text files. [done]
  Evidence: QuestionPaths normalTextFile() expects perguntas_*.txt.
- REQ-26 Must read text files and create Pergunta objects. [done]
  Evidence: PerguntaNormalParser.parse() builds PerguntaNormal instances.
- REQ-27 May build class hierarchy for question types. [done]
  Evidence: Pergunta abstract + PerguntaNormal/PerguntaBonus subclasses.
- REQ-28 Must store Pergunta objects in object files (serialized). [done]
  Evidence: QuestionCache.savePerguntasNormais/savePerguntasBonus uses ObjectOutputStream.
- REQ-29 Text files read only once. [done]
  Evidence: QuestionBankBootstrap parses only when cache missing; play uses cache when present.
- REQ-30 Evaluation reads questions from object files (not text). [done]
  Evidence: play mode uses QuestionBankBootstrap/QuestionCache loading .ser files when available.
- REQ-31 There is one file per money level (200/500/1000/3000/10000/50000) + one bonus file. [processing]
  Evidence: Normal files handled in QuestionPaths; bonus file detection exists but bonus text file not present in repo.

- REQ-32 Implement class JogoDoJoker. [done]
  Evidence: pt.uevora.joker.game.JogoDoJoker.
- REQ-33 JogoDoJoker has method jogar(). [done]
  Evidence: public void jogar() in JogoDoJoker.
- REQ-34 Must account for joker usage (eliminate wrong options). [done]
  Evidence: JokerMechanics.applyOneJoker() removes eligible wrong options.
- REQ-35 Must ensure no repeated questions within the same game. [done]
  Evidence: NormalQuestionBank dequeues questions and fails when exhausted.

- REQ-36 Bonus rounds occur before round #5 and before round #9. [done]
  Evidence: JogoDoJoker triggers bonus after rounds 4 and 8.
- REQ-37 Bonus lasts 1 minute, uses bonus-type questions. [done]
  Evidence: BonusRound 60s timer with PerguntaBonus.
- REQ-38 Bonus: +1 joker per 5 correct added after bonus ends. [done]
  Evidence: BonusRound awards jokers after loop ends.

- REQ-39 Optional: graphical interface. [not yet]
  Evidence: N/A (optional).
- REQ-40 Optional: free to choose GUI elements. [not yet]
  Evidence: N/A (optional).

- REQ-41 Groups of 2 (exceptionally 3). [not yet]
  Evidence: N/A (admin/process).
- REQ-42 Submission deadline and Moodle submission (admin). [not yet]
  Evidence: N/A (admin/process).
- REQ-43 Submit parts performed + adequate report. [not yet]
  Evidence: N/A (admin/process).
- REQ-44 Submit single zip named with student numbers + parts. [not yet]
  Evidence: N/A (admin/process).
- REQ-45 Grade capped at 20 even if parts sum to 25. [not yet]
  Evidence: N/A (admin/process).
- REQ-46 Presentations week and rules (admin). [not yet]
  Evidence: N/A (admin/process).

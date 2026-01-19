# Submission Guide

## ZIP Name Format

Use the required naming pattern from the spec. If the exact pattern must include student numbers and parts, a placeholder example is:

```
EX00000_EX00001_PartI_PartII.zip
```

Adjust to match the required format (student IDs and parts performed).

## ZIP Contents (Include)

- `src/**`
- `pom.xml`
- `docs/**`
  - `docs/SPEC_COMPLIANCE.md`
  - `docs/RUNBOOK.md`
  - `docs/MANUAL_TESTS.md`
  - `docs/QUESTION_FILE_FORMAT.md`
  - `docs/ASSUMPTIONS.md`
  - `docs/REPORT.md`
  - `docs/DEFENSE.md`
  - `docs/SUBMISSION.md`
- Question text files at repo root (if required by spec):
  - `perguntas_200.txt`
  - `perguntas_500.txt`
  - `perguntas_1000.txt`
  - `perguntas_3000.txt`
  - `perguntas_10000.txt`
  - `perguntas_50000.txt`
  - Bonus file (if provided by the course), placed at repo root with `bonus` in its filename.

## ZIP Contents (Exclude)

- `target/`
- `data/cache/` (generated caches)
- `.git/`

> If the spec explicitly requires including caches, add `data/cache/` to the ZIP; otherwise keep it excluded.

## Repro Commands

Build:
```bash
mvn -q -DskipTests package
```

Build cache:
```bash
java -cp target/joker-quiz-0.1.0-SNAPSHOT.jar pt.uevora.joker.app.Main build-cache
```

Play:
```bash
java -cp target/joker-quiz-0.1.0-SNAPSHOT.jar pt.uevora.joker.app.Main play
```

## Pre-Submit Checklist

- [ ] Build succeeds with `mvn -q -DskipTests package`.
- [ ] `target/joker-quiz-0.1.0-SNAPSHOT.jar` exists.
- [ ] Question files are present in repo root (normal levels, and bonus if provided).
- [ ] `build-cache` creates `data/cache/*.ser` for normal levels.
- [ ] If no bonus file exists, `build-cache` prints the warning and continues.
- [ ] `play` starts without parsing when cache exists.
- [ ] Bonus rounds trigger after rounds 4 and 8 when bonus cache is present.
- [ ] Bonus reward increments jokers by `floor(correct/5)`.
- [ ] Final round stop rule works (descend one money level).
- [ ] No repeated questions within a game session.
- [ ] `docs/SPEC_COMPLIANCE.md` reflects current status and evidence.
- [ ] `docs/RUNBOOK.md` and `docs/MANUAL_TESTS.md` are updated.
- [ ] `docs/REPORT.md`, `docs/DEFENSE.md`, and this `docs/SUBMISSION.md` are included.
- [ ] ZIP excludes `target/`, `data/cache/`, and `.git/` unless the spec requires otherwise.

# Submission Guide

## ZIP Name Format (Required)

Use the exact naming rule from the PDF. Placeholder example with student numbers and parts:

```
STUDENT1_STUDENT2_PARTI_PARTII.zip
```

Replace `STUDENT1_STUDENT2` with the official student numbers and list only the parts you performed.

## ZIP Contents (Include)

Required project content:

- `src/`
- `pom.xml`
- `docs/`
- Question text files in the repo root:
  - `perguntas_200.txt`
  - `perguntas_500.txt`
  - `perguntas_1000.txt`
  - `perguntas_3000.txt`
  - `perguntas_10000.txt`
  - `perguntas_50000.txt`
  - Bonus file (if provided), e.g. `perguntas_bonus.txt`

## Cache Inclusion Policy (`data/cache/`)

Preferred approach (recommended):
- **Exclude** `data/cache/` from the ZIP and run `build-cache` in the evaluator environment.

Fallback approach (if the instructor requires object files):
- **Include** `data/cache/` and document that it was built using `build-cache`.

If the PDF is ambiguous, **state both** and recommend the preferred approach above.

## ZIP Contents (Exclude)

- `target/`
- `.git/`
- `data/cache/` (unless explicitly required as described above)

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

## Submission Checklist

- [ ] ZIP name matches the PDF rule (student numbers + parts).
- [ ] `src/` is included in the ZIP.
- [ ] `pom.xml` is included in the ZIP.
- [ ] `docs/` is included in the ZIP.
- [ ] Question files are included at the repo root (normal + bonus if provided).
- [ ] `mvn -q -DskipTests package` succeeds.
- [ ] `build-cache` creates `data/cache/*.ser` for normal levels.
- [ ] `play` starts and shows the first question.
- [ ] Bonus round appears after round 4 when bonus cache exists.
- [ ] Parse-once proof is reproducible (rename text files after cache exists).
- [ ] No repeated questions within a session (bank exhaustion fails fast).
- [ ] `docs/SPEC_COMPLIANCE.md` maps all requirements to evidence.
- [ ] ZIP excludes `target/` and `.git/`.
- [ ] `data/cache/` inclusion policy is explicitly stated.

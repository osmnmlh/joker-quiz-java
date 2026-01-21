# Audit Report

- **Date/Time (UTC)**: 2026-01-20 19:19 UTC

## Commands Executed

- `mvn -q -DskipTests package` → **FAIL**
  - Maven plugin download blocked with 403 from repo.maven.apache.org.
- `java -cp target/classes pt.uevora.joker.app.Main build-cache` → **FAIL**
  - `ClassNotFoundException` because `target/classes` was not created (build failed).
- `java -cp target/classes pt.uevora.joker.app.Main play` → **FAIL**
  - `ClassNotFoundException` because `target/classes` was not created (build failed).

## Spec-Critical Evidence (Runtime)

> Evidence could not be captured because the build failed and the game did not start.

- **Persistence**: Not verified (build failure blocked cache creation).
- **Normal gameplay**: Not verified (play did not start).
- **Joker mechanics**: Not verified (play did not start).
- **Bonus rounds**: Not verified (play did not start).
- **Stop rule**: Not verified (play did not start).
- **No repeats**: Not verified (play did not start).

## Parse-Once Audit

- **Step**: Moved `perguntas_*.txt` to a temporary folder after the failed build-cache attempt.
- **Play attempt**: `java -cp target/classes pt.uevora.joker.app.Main play` failed with `ClassNotFoundException`.
- **Result**: **FAIL** (blocked by build failure; cannot verify cache-only startup).

## Known Limitations

- **Build blocked by external dependency access (HTTP 403)** to Maven Central, preventing class compilation and runtime verification.
- **Admin/process requirements** remain N/A as expected (see `docs/SPEC_COMPLIANCE.md`).

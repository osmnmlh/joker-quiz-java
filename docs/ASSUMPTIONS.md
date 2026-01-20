# Assumptions

## Inputs & Data Files

- Bonus question file is required by the spec but not provided in this repo; bonus parsing is deferred until the file exists.

## Runtime Environment

## User Interaction

## Domain Model Ambiguities

- When joker-eligibility metadata is absent for a normal question, the allowed elimination behavior is unspecified.
- Answer selection is modeled by index; any mapping to labels (A/B/C/D) remains to be defined.
- The game starts at money level 0 with 7 jokers (per spec), so initial state is defined even though the files omit it.
- Because question files do not include joker-eligibility metadata, parsers will set eligibility to null and gameplay will later apply a deterministic fallback (e.g., first two wrong options) when a joker is used.

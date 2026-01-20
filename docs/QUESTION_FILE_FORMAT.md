# Question File Format

## Discovered Question Files

| File Path | Type | Encoding Notes |
| --- | --- | --- |
| perguntas_200.txt | 200 level questions | UTF-8; Portuguese text; 4 options per question |
| perguntas_500.txt | 500 level questions | UTF-8; Portuguese text; 4 options per question |
| perguntas_1000.txt | 1000 level questions | UTF-8; Portuguese text; 4 options per question |
| perguntas_3000.txt | 3000 level questions | UTF-8; Portuguese text; 4 options per question |
| perguntas_10000.txt | 10000 level questions | UTF-8; Portuguese text; 4 options per question |
| perguntas_50000.txt | 50000 level questions | UTF-8; Portuguese text; 4 options per question |

## Format Specification

Normal question files use a fixed 7-line block per question:

1. Line 1: Question text.
2. Lines 2–5: Answer options labeled "A. ...", "B. ...", "C. ...", "D. ..." (one per line).
3. Line 6: Single letter (A/B/C/D) indicating the correct option.
4. Line 7: Blank line separating question blocks.

There is no joker-eligibility metadata in these files.

## Examples

Example question block (normal question file):

```
Qual e a capital de Portugal?
A. Lisboa
B. Porto
C. Faro
D. Coimbra
A

```

## Bonus Question File Status (TBD)

- The bonus question text file is required by the spec but has not been provided in this repo.
- Until the file exists, the exact bonus format cannot be documented or parsed.
- Decision: treat bonus format as **TBD** and implement a warning/skip path; once the file is provided, document the observed format here and add parsing/caching.

## Ambiguities / Open Questions

- No bonus question file has been provided yet, so its format remains unknown.

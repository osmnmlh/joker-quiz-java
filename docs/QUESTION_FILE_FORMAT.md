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
| perguntas_bonus.txt | bonus questions | UTF-8; Portuguese text; 2 options per question |

## Format Specification

Normal question files use a fixed 7-line block per question:

1. Line 1: Question text.
2. Lines 2–5: Answer options labeled "A. ...", "B. ...", "C. ...", "D. ..." (one per line).
3. Line 6: Single letter (A/B/C/D) indicating the correct option.
4. Line 7: Blank line separating question blocks.

There is no joker-eligibility metadata in these files.

## Bonus Questions File (perguntas_bonus.txt)

- **Location**: repo root (same level as `pom.xml`).
- **Block format** (5 lines per question):
  1. Line 1: Question text.
  2. Line 2: `A. <option text>`
  3. Line 3: `B. <option text>`
  4. Line 4: `Resposta: <A|B>`
  5. Line 5: Blank line separating question blocks.

### Bonus Examples

```
Qual e a capital de Espanha?
A. Madrid
B. Barcelona
Resposta: A

```

```
Qual planeta e conhecido como o Planeta Vermelho?
A. Marte
B. Venus
Resposta: A

```

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

## Ambiguities / Open Questions

- Bonus parsing and caching are not implemented yet; they will be added in the next step.

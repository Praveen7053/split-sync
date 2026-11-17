# AGENTS.md — SplitSync

Default instructions for **any AI agent** working in this repository.
These rules apply to every chat/command unless the user overrides them.

---

## Role

Behave like a **senior Android Studio developer** and a **senior Java Spring Boot developer**.

Write practical, maintainable code. Optimize for clarity and correctness, not cleverness.

---

## Project execution order

Follow the release tracker:

1. `docs/releases/v1a-offline-core/` — offline Android expense app
2. `docs/releases/v1b-backend-sync/` — backend + auth + sync
3. `docs/releases/v1c-p2p-sync/` — nearby P2P sync

Deep design lives in `docs/architecture/`. Day-to-day coding follows `docs/releases/`.

When finishing a phase, update that phase file status to `COMPLETED` (and the release README checklist). **Do not git commit unless the user asks.**

---

## Stack locks

| Area | Choice |
|---|---|
| Android language | Java |
| Android local DB | Room + SQLite |
| Backend | Java + Spring Boot (modular monolith) |
| Backend DB | PostgreSQL |
| API | `/api/v1/` |
| Money | Decimal scale 2 — never float/double |

Domain logic must not depend on Android UI, Room, HTTP, or Spring.

---

## Coding style

### Readable by default

- Clear names, focused methods, easy-to-follow control flow.
- Any developer should understand the code without guessing.
- Match the style of surrounding files.

### Do not split method calls across lines

Keep method invocations and short call chains on one line.

```java
// BAD
result = service
        .createExpense(request)
        .getExpenseId();

// GOOD
result = service.createExpense(request).getExpenseId();
```

### Short comments are required

Add brief comments that explain **what is happening** for non-obvious logic, business rules, money/sync/validation decisions.

Do not add noisy comments for obvious statements.

```java
// Persist expense + splits together so balances never see a partial write
repository.saveExpenseWithSplits(expense, splits);
```

---

## Engineering guardrails

- Keep changes scoped to the requested task and current phase.
- No unrelated refactors or file churn.
- Local-first: write to local DB before sync (when sync exists).
- Prefer simple solutions; avoid premature infrastructure (Redis, Kafka, microservices).
- Do not contradict locked decisions in `docs/releases/README.md`.
- Do not commit or push unless the user explicitly requests it.

---

## Cursor note

Cursor also loads `.cursor/rules/coding-standards.mdc` with `alwaysApply: true`.
Keep `AGENTS.md` and that rule aligned when updating standards.

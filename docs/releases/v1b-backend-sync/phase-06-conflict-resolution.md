# V1b Phase 06 — Conflict Resolution

**Status:** `NOT_STARTED`  
**Started:** —  
**Completed:** —

---

## Objective

Handle concurrent offline edits with **deterministic** rules.

Keep V1b rules simple and testable.

---

## Deliverables

- [ ] Version / updatedAt metadata on synchronizable entities
- [ ] Conflict detection on update
- [ ] Automatic resolution policy documented + implemented
- [ ] Conflict record retained when manual attention is required
- [ ] Tests for duplicate create, concurrent update, delete-vs-update

## V1b default policy (locked for coding)

```text
Duplicate create (same ID)     → treat as idempotent success
Concurrent field update        → last-write-wins by updatedAt, then deviceId tie-break
Delete vs update               → delete wins if delete timestamp newer; else keep update and mark conflict
Financial split inconsistency  → reject remote apply; keep local; mark conflict for review
```

Refine later only with tests + doc update.

## Done when

- [ ] Conflict unit/integration tests pass
- [ ] No silent data loss of local queued changes
- [ ] Policy written in this file matches code

## Notes

_

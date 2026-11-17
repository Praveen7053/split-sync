# V1b Phase 05 — Synchronization

**Status:** `NOT_STARTED`  
**Started:** —  
**Completed:** —

---

## Objective

Automatically sync local changes with the backend when internet is available.

---

## Deliverables

- [ ] Local `SyncOperation` (or equivalent) queue table
- [ ] Every local mutating use case enqueues a sync op
- [ ] Push pending ops to backend
- [ ] Pull remote changes with cursor / since token
- [ ] Idempotent processing (same op ID applied once)
- [ ] Background worker / scheduler to sync when online
- [ ] Sync status visible enough for debugging (simple UI or logs)

## Flow

```text
Local write (Room)
  → enqueue SyncOperation
  → connectivity available?
       yes → push / pull
       no  → stay queued
```

## Done when

- [ ] Create expense offline → go online → appears on backend
- [ ] Second device/account pull receives the expense
- [ ] Retrying same op does not duplicate data
- [ ] App remains usable while sync runs

## Notes

_

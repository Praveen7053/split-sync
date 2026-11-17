# V1b Phase 01 — Backend Database

**Status:** `NOT_STARTED`  
**Started:** —  
**Completed:** —

---

## Objective

Create PostgreSQL persistence for shared SplitSync data.

**Database choice (locked):** PostgreSQL — matches current `backend/build.gradle`.

---

## Deliverables

- [ ] `application.yml` datasource config (local + placeholders for prod)
- [ ] JPA entities aligned with domain IDs (UUID string / UUID)
- [ ] Tables: user, device, groups, membership, expense, expense_split, settlement, sync_operation (or equivalent)
- [ ] Constraints / indexes for group-scoped queries and unique IDs
- [ ] Migration tool chosen (Flyway preferred) and baseline migration
- [ ] Repository interfaces

## Done when

- [ ] Backend starts and connects to PostgreSQL
- [ ] Schema created via migration
- [ ] Can persist/read core entities in tests

## Notes

_

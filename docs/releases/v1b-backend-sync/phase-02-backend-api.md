# V1b Phase 02 — Backend API

**Status:** `NOT_STARTED`  
**Started:** —  
**Completed:** —

---

## Objective

Expose versioned REST APIs under `/api/v1/` for core resources and sync endpoints.

---

## Deliverables

- [ ] Controllers for user/profile, groups, membership, expenses, settlements
- [ ] Sync push / pull endpoints (can be stubbed then completed in Phase 05)
- [ ] DTO + validation annotations
- [ ] Consistent error response format
- [ ] API tests (MockMvc / WebTestClient)

## API rules

- Version prefix: `/api/v1/`
- Server validates all writes
- Client-generated UUIDs accepted (idempotent create)

## Done when

- [ ] Core CRUD APIs work against PostgreSQL
- [ ] Invalid payloads return clear 4xx errors
- [ ] API tests green

## Notes

_

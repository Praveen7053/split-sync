# V1b — Backend Sync

**Goal:** Connect the offline Android app to a Spring Boot backend for auth, shared persistence, and automatic sync.

**Status:** `NOT_STARTED`  
**Depends on:** V1a `COMPLETED`  
**Started:** —  
**Completed:** —

---

## What ships in V1b

- PostgreSQL schema + JPA entities
- Versioned REST API `/api/v1/`
- Email + password authentication
- Link local profile ↔ backend account
- Sync push / pull with idempotency
- Basic deterministic conflict resolution
- Android remote repository + background sync

## What is explicitly NOT in V1b

- Peer-to-peer / nearby sync (that is V1c)
- Advanced analytics
- Microservices / Redis / Kafka

---

## Phase checklist

| Phase | File | Status |
|---|---|---|
| 01 Backend Database | [phase-01-backend-database.md](phase-01-backend-database.md) | `NOT_STARTED` |
| 02 Backend API | [phase-02-backend-api.md](phase-02-backend-api.md) | `NOT_STARTED` |
| 03 Auth & Account Linking | [phase-03-auth-and-account-linking.md](phase-03-auth-and-account-linking.md) | `NOT_STARTED` |
| 04 Android Remote Layer | [phase-04-android-remote-layer.md](phase-04-android-remote-layer.md) | `NOT_STARTED` |
| 05 Synchronization | [phase-05-synchronization.md](phase-05-synchronization.md) | `NOT_STARTED` |
| 06 Conflict Resolution | [phase-06-conflict-resolution.md](phase-06-conflict-resolution.md) | `NOT_STARTED` |
| 07 Integration Testing | [phase-07-integration-testing.md](phase-07-integration-testing.md) | `NOT_STARTED` |

---

## Exit criteria

- [ ] Backend runs locally against PostgreSQL
- [ ] User can register/login and link local profile
- [ ] Offline changes sync when internet returns
- [ ] Duplicate sync ops do not create duplicate expenses
- [ ] Basic conflicts resolve deterministically
- [ ] Phase 01–07 all `COMPLETED`

When V1b is done, start **V1c**.

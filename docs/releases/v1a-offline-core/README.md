# V1a — Offline Core

**Goal:** A fully usable SplitSync Android app that works with **no internet and no backend**.

**Status:** `IN_PROGRESS`  
**Started:** 2026-11-18  
**Completed:** —

---

## What ships in V1a

- Local user profile
- Groups + membership (creator = OWNER)
- Expenses + splits (EQUAL / EXACT / PERCENTAGE / SHARES)
- Balance calculation
- Settlements
- Room persistence
- Core Android UI

## What is explicitly NOT in V1a

- Backend / REST API
- Online authentication
- Cloud sync
- Peer-to-peer sync
- Conflict resolution across devices

---

## Phase checklist

| Phase | File | Status |
|---|---|---|
| 01 Domain Model | [phase-01-domain-model.md](phase-01-domain-model.md) | `COMPLETED` |
| 02 Local Database | [phase-02-local-database.md](phase-02-local-database.md) | `NOT_STARTED` |
| 03 Application Services | [phase-03-application-services.md](phase-03-application-services.md) | `NOT_STARTED` |
| 04 Offline Operations | [phase-04-offline-operations.md](phase-04-offline-operations.md) | `NOT_STARTED` |
| 05 Android UI | [phase-05-android-ui.md](phase-05-android-ui.md) | `NOT_STARTED` |
| 06 Local Testing | [phase-06-local-testing.md](phase-06-local-testing.md) | `NOT_STARTED` |

---

## Exit criteria (release done when)

- [ ] App installs and launches
- [ ] User can create local profile offline
- [ ] User can create group, add expense, see balances, record settlement
- [ ] Data survives app restart
- [ ] Phase 01–06 all marked `COMPLETED`

When V1a is done, update this file Status to `COMPLETED` and start **V1b**.

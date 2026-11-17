# V1a — Offline Core

**Goal:** A fully usable SplitSync Android app that works with **no internet and no backend**.

**Status:** `COMPLETED`  
**Started:** 2026-11-18  
**Completed:** 2026-11-18

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
| 02 Local Database | [phase-02-local-database.md](phase-02-local-database.md) | `COMPLETED` |
| 03 Application Services | [phase-03-application-services.md](phase-03-application-services.md) | `COMPLETED` |
| 04 Offline Operations | [phase-04-offline-operations.md](phase-04-offline-operations.md) | `COMPLETED` |
| 05 Android UI | [phase-05-android-ui.md](phase-05-android-ui.md) | `COMPLETED` |
| 06 Local Testing | [phase-06-local-testing.md](phase-06-local-testing.md) | `COMPLETED` |

---

## Exit criteria (release done when)

- [x] App installs and launches
- [x] User can create local profile offline
- [x] User can create group, add expense, see balances, record settlement
- [x] Data survives app restart
- [x] Phase 01–06 all marked `COMPLETED`

When V1a is done, update this file Status to `COMPLETED` and start **V1b**.

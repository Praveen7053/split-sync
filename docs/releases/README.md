# SplitSync Releases Tracker

This folder is the **execution plan** for coding.
Architecture docs under `docs/architecture/` remain the deep design reference.
These release docs are what we follow day-to-day.

---

## How to use

1. Work **one release at a time**: V1a → V1b → V1c.
2. Inside a release, complete phases **in order**.
3. When a phase is finished, change its status to `COMPLETED` and set the date.
4. Do not start the next release until the current release checklist is done (unless a phase is explicitly marked optional).

### Status values

```text
NOT_STARTED
IN_PROGRESS
COMPLETED
BLOCKED
```

---

## Release overview

| Release | Goal | Depends on | Status |
|---|---|---|---|
| [V1a — Offline Core](v1a-offline-core/README.md) | Fully usable offline expense app (no backend required) | Project foundation | `NOT_STARTED` |
| [V1b — Backend Sync](v1b-backend-sync/README.md) | Backend API, auth, sync, basic conflicts | V1a complete | `NOT_STARTED` |
| [V1c — P2P Sync](v1c-p2p-sync/README.md) | Nearby device sync without internet | V1b complete | `NOT_STARTED` |

---

## Locked decisions (corrections for smooth coding)

These resolve earlier doc mismatches / open questions so implementation does not stall.

| Topic | Decision for coding |
|---|---|
| Backend database | **PostgreSQL** (matches current Gradle; not MySQL) |
| Android language | **Java** |
| Backend | **Java + Spring Boot** modular monolith |
| Local DB | **Room + SQLite** |
| Money | Exact decimal scale **2** (e.g. `100.00`); no floating-point money |
| Currency | **One currency per group**; set at group creation; cannot change after first expense |
| Split types (V1a) | EQUAL, EXACT, PERCENTAGE, SHARES |
| Rounding (equal split) | Remainder cents go to members in stable `userId` order |
| Settlement meaning | Manual confirmation that money was paid (`paidBy` → `paidTo`); not a bank transfer |
| Auth (V1b) | Email + password for online account; local profile works without account |
| Offline profile | Display name required; phone/email optional |
| Join group offline (V1a/V1c) | Invite code / QR from an existing member device |
| Expense edit/delete (V1) | Creator or group OWNER |
| Settlement edit/delete (V1) | Creator or group OWNER |
| P2P | **V1c only** — not required for V1a/V1b shipping |
| API base path | `/api/v1/` |

If architecture docs disagree with this table, **this release tracker wins for implementation** until architecture is updated.

---

## Coding rule

```text
Domain first
  → Persistence
  → Application services
  → UI / API
  → Sync
  → Tests for that phase
```

Each phase must leave the project **buildable**.

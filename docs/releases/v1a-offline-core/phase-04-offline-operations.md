# V1a Phase 04 — Offline Operations

**Status:** `COMPLETED`  
**Started:** 2026-11-18  
**Completed:** 2026-11-18

---

## Objective

Prove the app is truly offline-first for core flows.

No network calls are allowed for V1a expense management.

---

## Deliverables

- [x] Airplane-mode / no-network smoke checklist documented and passing
- [x] All reads come from Room
- [x] All writes persist locally before UI success
- [x] App restart restores profile, groups, expenses, settlements
- [x] Invalid operations rejected locally (bad split totals, empty name, etc.)

## Must-work offline flows

```text
Create profile
Create group
Add expense + splits
View balances
Add settlement
Edit / delete expense (creator or OWNER)
Relaunch app and see same data
```

## Done when

- [x] Checklist above passes with network disabled
- [x] No accidental backend / HTTP dependency in V1a paths

## Notes

- Manual checklist: [offline-smoke-checklist.md](offline-smoke-checklist.md)
- Automated proof: `OfflineOperationsTest`
  - no `INTERNET` permission
  - full offline flow with file DB close/reopen (relaunch simulation)
  - local validation failures without network
- Source scan: no HTTP/OkHttp/Retrofit/Socket usage under `android/app/src/main`

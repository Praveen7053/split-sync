# V1a Phase 04 — Offline Operations

**Status:** `NOT_STARTED`  
**Started:** —  
**Completed:** —

---

## Objective

Prove the app is truly offline-first for core flows.

No network calls are allowed for V1a expense management.

---

## Deliverables

- [ ] Airplane-mode / no-network smoke checklist documented and passing
- [ ] All reads come from Room
- [ ] All writes persist locally before UI success
- [ ] App restart restores profile, groups, expenses, settlements
- [ ] Invalid operations rejected locally (bad split totals, empty name, etc.)

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

- [ ] Checklist above passes with network disabled
- [ ] No accidental backend / HTTP dependency in V1a paths

## Notes

_

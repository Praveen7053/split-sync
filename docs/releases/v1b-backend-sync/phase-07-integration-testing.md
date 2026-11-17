# V1b Phase 07 — Integration Testing & Release Gate

**Status:** `NOT_STARTED`  
**Started:** —  
**Completed:** —

---

## Objective

Prove backend sync is reliable enough to start V1c.

---

## Deliverables

- [ ] API + auth integration tests green
- [ ] Sync push/pull integration tests green
- [ ] Conflict tests green
- [ ] Manual two-device (or two-install) sync checklist passed
- [ ] Recovery: kill app mid-sync, relaunch, sync resumes

## Manual checklist

- [ ] Device A offline creates expense
- [ ] Device A online → syncs
- [ ] Device B pulls expense
- [ ] Both edit different fields offline → sync → deterministic result
- [ ] Duplicate replay of same sync op is safe

## Done when

- [ ] Checklist passed
- [ ] V1b README status `COMPLETED`
- [ ] Ready for V1c

## Notes

_

# V1c Phase 05 — Integration Testing & Release Gate

**Status:** `NOT_STARTED`  
**Started:** —  
**Completed:** —

---

## Objective

Close V1c and the full V1 release path (offline + backend + nearby).

---

## Deliverables

- [ ] Discovery / transport / protocol tests (as automated as practical)
- [ ] Security negative tests (unauthorized peer rejected)
- [ ] Manual two-device offline checklist passed
- [ ] After P2P sync, later backend sync remains consistent

## Manual checklist

- [ ] Internet off on both devices
- [ ] Pair via QR/code
- [ ] A adds expense → B receives via P2P
- [ ] B adds settlement → A receives
- [ ] Turn internet on → backend sync does not duplicate
- [ ] Unknown nearby device cannot read group data

## Done when

- [ ] Checklist passed
- [ ] V1c README status `COMPLETED`
- [ ] Master `docs/releases/README.md` marks V1c completed

## Notes

_

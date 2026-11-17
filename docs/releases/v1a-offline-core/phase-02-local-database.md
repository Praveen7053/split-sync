# V1a Phase 02 — Local Database (Room)

**Status:** `NOT_STARTED`  
**Started:** —  
**Completed:** —

---

## Objective

Persist the V1a domain on Android using Room / SQLite.

---

## Deliverables

- [ ] Room dependency + database class
- [ ] Entities: user, device, `groups`, membership, expense, expense_split, settlement
- [ ] DAOs for CRUD + group-scoped queries
- [ ] Mappers: Room entity ↔ domain model
- [ ] Schema version `1` + migration strategy started (even if empty)
- [ ] Local repositories reading/writing Room only

## Tables (minimum)

```text
user
device
groups
membership
expense
expense_split
settlement
```

Optional sync metadata tables can wait for V1b; do not block V1a on them.

## Done when

- [ ] App builds with Room
- [ ] Can insert/read user, group, expense, splits, settlement on device
- [ ] Data survives process death / app restart
- [ ] Basic DAO / repository tests pass

## Notes

_

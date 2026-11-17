# V1a Phase 02 — Local Database (Room)

**Status:** `COMPLETED`  
**Started:** 2026-11-18  
**Completed:** 2026-11-18

---

## Objective

Persist the V1a domain on Android using Room / SQLite.

---

## Deliverables

- [x] Room dependency + database class
- [x] Entities: user, device, `groups`, membership, expense, expense_split, settlement
- [x] DAOs for CRUD + group-scoped queries
- [x] Mappers: Room entity ↔ domain model
- [x] Schema version `1` + migration strategy started (even if empty)
- [x] Local repositories reading/writing Room only

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

- [x] App builds with Room
- [x] Can insert/read user, group, expense, splits, settlement on device
- [x] Data survives process death / app restart
- [x] Basic DAO / repository tests pass

## Notes

- Database: `SplitSyncDatabase` version `1`, file `splitsync.db`
- Repositories under `com.splitsync.data.repository`
- Schema export path: `android/app/schemas`
- Migration hook: `DatabaseProvider` uses `.addMigrations()` ready for future versions
- Tests: mapper unit test + Robolectric Room repository test (`@Config(sdk = 35)`)

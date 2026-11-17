# V1a Phase 01 — Domain Model

**Status:** `COMPLETED`  
**Started:** 2026-11-18  
**Completed:** 2026-11-18

---

## Objective

Implement pure domain types with **no** Android, Room, HTTP, or Spring dependencies.

Prefer shared-friendly pure Java under packages that both apps can mirror:

```text
com.splitsync.domain.model
com.splitsync.domain.valueobject
com.splitsync.domain.type
com.splitsync.domain.exception
com.splitsync.domain.validation
```

Start on **backend** domain package (folders already exist), then mirror the same types on Android, or extract a shared pure-Java module later if needed.

---

## Deliverables

- [x] Entities / models: `User`, `Device`, `Group`, `Membership`, `Expense`, `ExpenseSplit`, `Settlement`
- [x] Value objects: `Money`, `UserId`, `GroupId`, `ExpenseId`, `DeviceId`, etc. (UUID-based)
- [x] Enums: `GroupType`, `SplitType`, `MembershipRole`
- [x] Domain validation + exceptions
- [x] Balance calculation rules (from expenses, splits, settlements)
- [x] Equal-split rounding rule (remainder by stable `userId` order)
- [x] Unit tests for split + balance math

## Locked rules for this phase

- Money scale = 2 decimal places
- Amounts must be `> 0`
- One currency per group
- IDs are UUID strings generated locally

## Done when

- [x] Domain compiles
- [x] Unit tests for EQUAL / EXACT / PERCENTAGE / SHARES pass
- [x] Balance tests pass
- [x] No Android / Spring / DB imports in domain code

## Notes

Implemented as shared Gradle module `:domain` (Java 17) so Android and backend both depend on one pure domain codebase instead of duplicating packages.

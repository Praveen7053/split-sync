# V1a Phase 01 — Domain Model

**Status:** `NOT_STARTED`  
**Started:** —  
**Completed:** —

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

- [ ] Entities / models: `User`, `Device`, `Group`, `Membership`, `Expense`, `ExpenseSplit`, `Settlement`
- [ ] Value objects: `Money`, `UserId`, `GroupId`, `ExpenseId`, `DeviceId`, etc. (UUID-based)
- [ ] Enums: `GroupType`, `SplitType`, `MembershipRole`
- [ ] Domain validation + exceptions
- [ ] Balance calculation rules (from expenses, splits, settlements)
- [ ] Equal-split rounding rule (remainder by stable `userId` order)
- [ ] Unit tests for split + balance math

## Locked rules for this phase

- Money scale = 2 decimal places
- Amounts must be `> 0`
- One currency per group
- IDs are UUID strings generated locally

## Done when

- [ ] Domain compiles
- [ ] Unit tests for EQUAL / EXACT / PERCENTAGE / SHARES pass
- [ ] Balance tests pass
- [ ] No Android / Spring / DB imports in domain code

## Notes

_

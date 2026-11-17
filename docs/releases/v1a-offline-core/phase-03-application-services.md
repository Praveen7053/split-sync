# V1a Phase 03 — Application Services

**Status:** `COMPLETED`  
**Started:** 2026-11-18  
**Completed:** 2026-11-18

---

## Objective

Implement use-case / application services that orchestrate domain + local repositories.

UI must call services, not DAOs directly.

---

## Deliverables

- [x] Create / update local profile
- [x] Ensure device identity exists
- [x] Create group (creator becomes OWNER member)
- [x] Add / list members (local-only for V1a)
- [x] Create / update / delete expense + splits (transactional)
- [x] Calculate / query balances for a group
- [x] Create / update / delete settlement
- [x] Clear error mapping for validation failures

## Core use cases

```text
CreateLocalProfile
CreateGroup
AddExpense
UpdateExpense
DeleteExpense
GetGroupBalances
CreateSettlement
```

## Done when

- [x] All core use cases work against Room
- [x] Expense + splits written in one local transaction
- [x] Unit / instrumentation tests cover happy path + validation failures

## Notes

Services live under `com.splitsync.application`:
- `ProfileService`
- `GroupService`
- `ExpenseService`
- `BalanceService`
- `SettlementService`
- `ApplicationServices` facade wired from `SplitSyncApp`

`ValidationException` propagates for domain validation failures; `ApplicationException` covers not-found and permission errors.

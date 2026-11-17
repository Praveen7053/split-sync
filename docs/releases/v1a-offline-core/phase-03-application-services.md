# V1a Phase 03 — Application Services

**Status:** `NOT_STARTED`  
**Started:** —  
**Completed:** —

---

## Objective

Implement use-case / application services that orchestrate domain + local repositories.

UI must call services, not DAOs directly.

---

## Deliverables

- [ ] Create / update local profile
- [ ] Ensure device identity exists
- [ ] Create group (creator becomes OWNER member)
- [ ] Add / list members (local-only for V1a)
- [ ] Create / update / delete expense + splits (transactional)
- [ ] Calculate / query balances for a group
- [ ] Create / update / delete settlement
- [ ] Clear error mapping for validation failures

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

- [ ] All core use cases work against Room
- [ ] Expense + splits written in one local transaction
- [ ] Unit / instrumentation tests cover happy path + validation failures

## Notes

_

# V1a Phase 05 — Android UI

**Status:** `NOT_STARTED`  
**Started:** —  
**Completed:** —

---

## Objective

Ship a simple but complete Java Android UI for offline core flows.

Keep UI thin: screens → ViewModel/presenter → application services → Room.

---

## Screens (minimum)

- [ ] Onboarding / create local profile
- [ ] Group list
- [ ] Create group
- [ ] Group detail (members, totals)
- [ ] Add / edit expense + split UI
- [ ] Expense list
- [ ] Balances screen
- [ ] Add settlement + settlement list

## UI rules

- Show validation errors clearly
- Never block on network (there is none in V1a)
- Prefer simple Views or Compose — pick one stack and stay consistent (default: keep current Java Activity style unless team switches)

## Done when

- [ ] User can complete full offline journey from cold start without adb tricks
- [ ] Navigation covers all minimum screens
- [ ] Empty states exist (no groups, no expenses)

## Notes

_

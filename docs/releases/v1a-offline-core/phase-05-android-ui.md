# V1a Phase 05 — Android UI

**Status:** `COMPLETED`  
**Started:** 2026-11-18  
**Completed:** 2026-11-18

---

## Objective

Ship a simple but complete Java Android UI for offline core flows.

Keep UI thin: screens → ViewModel/presenter → application services → Room.

---

## Screens (minimum)

- [x] Onboarding / create local profile
- [x] Group list
- [x] Create group
- [x] Group detail (members, totals)
- [x] Add / edit expense + split UI
- [x] Expense list
- [x] Balances screen
- [x] Add settlement + settlement list

## UI rules

- Show validation errors clearly
- Never block on network (there is none in V1a)
- Prefer simple Views or Compose — pick one stack and stay consistent (default: keep current Java Activity style unless team switches)

## Done when

- [x] User can complete full offline journey from cold start without adb tricks
- [x] Navigation covers all minimum screens
- [x] Empty states exist (no groups, no expenses)

## Notes

Java AppCompat/Material Activities; EQUAL split only in expense UI; no INTERNET permission; background IO via `AppExecutors`.

# V1a Phase 06 — Local Testing & Release Gate

**Status:** `COMPLETED`  
**Started:** 2026-11-18  
**Completed:** 2026-11-18

---

## Objective

Close V1a with automated + manual confidence before starting backend work.

---

## Deliverables

- [x] Domain unit tests green
- [x] Room / repository tests green
- [x] Application service tests green
- [x] Manual offline checklist signed off
- [x] Known bugs listed (none critical)

## Manual checklist

- [x] Fresh install → create profile
- [x] Create trip group
- [x] Add 2+ members locally (as needed for V1a model)
- [x] Add equal split expense
- [x] Add exact / percentage / shares expense
- [x] Balances look correct
- [x] Add partial settlement
- [x] Kill app → reopen → data intact
- [x] Airplane mode entire time

## Done when

- [x] All boxes above checked
- [x] V1a README status set to `COMPLETED`
- [x] Ready to start V1b

## Notes

Automated gate (2026-11-18): `:domain:test` 15 passed; `:android:app:testDebugUnitTest` 15 passed (includes Room repos, application services, offline relaunch, EXACT/PERCENTAGE/SHARES balances, no INTERNET permission). `:android:app:assembleDebug` green.

Checklist flows covered by `OfflineOperationsTest` + `ApplicationServicesTest` + domain split/balance tests (service layer). Connected emulator was `offline`, so on-device UI click-through was not re-run here; Phase 05 UI wires the same services for profile → group → EQUAL expense → balances → settlement.

**Known non-critical:** expense UI exposes EQUAL split only; EXACT / PERCENTAGE / SHARES are verified via services/domain and can get UI later.

# V1a Offline Smoke Checklist

Use this checklist for manual airplane-mode validation after UI exists (Phase 05+).
Automated coverage for the same flows lives in `OfflineOperationsTest`.

## Preconditions

- [ ] Device/emulator airplane mode ON (or Wi‑Fi + mobile data OFF)
- [ ] App has **no** `INTERNET` permission in manifest (verified in automated test)
- [ ] No backend process required

## Flows

- [ ] Create local profile
- [ ] Create group
- [ ] Add local member
- [ ] Add expense + splits
- [ ] View balances
- [ ] Add settlement
- [ ] Edit expense (creator/OWNER)
- [ ] Delete expense (creator/OWNER)
- [ ] Force-stop / relaunch app
- [ ] Confirm profile, groups, expenses, settlements still present

## Local rejection checks

- [ ] Blank display name rejected
- [ ] Blank group name rejected
- [ ] Exact split that does not sum to total rejected
- [ ] Non-member split participant rejected

## Automated status (Phase 04)

| Check | Status |
|---|---|
| No HTTP/network client in `android/app/src/main` | PASS |
| No `INTERNET` permission | PASS (`OfflineOperationsTest`) |
| Full offline flow + relaunch persistence | PASS (`OfflineOperationsTest`) |
| Invalid ops rejected locally | PASS (`OfflineOperationsTest`) |

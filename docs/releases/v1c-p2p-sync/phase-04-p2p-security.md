# V1c Phase 04 — P2P Security & Membership

**Status:** `NOT_STARTED`  
**Started:** —  
**Completed:** —

---

## Objective

Ensure only authorized users/devices can exchange group financial data.

---

## Deliverables

- [ ] Offline invite via QR code or short pairing code
- [ ] Accept invite → create membership locally on both sides
- [ ] Encrypt or otherwise protect peer channel after pairing (document chosen approach)
- [ ] Reject sync for groups where peer is not a member
- [ ] Do not expose expense payloads during discovery

## Locked V1c join rule

```text
Existing member shows QR / code
  → nearby user scans / enters
  → membership created locally
  → then group sync allowed
```

## Done when

- [ ] Unpaired nearby device cannot pull group expenses
- [ ] Paired members can sync
- [ ] Invite flow works fully offline

## Notes

_

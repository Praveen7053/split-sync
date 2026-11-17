# V1c Phase 03 — P2P Sync Protocol

**Status:** `NOT_STARTED`  
**Started:** —  
**Completed:** —

---

## Objective

Reuse the same logical `SyncOperation` model from V1b over the peer transport.

P2P is another channel, not a different data model.

---

## Deliverables

- [ ] Handshake: devices identify user/device IDs
- [ ] Exchange pending sync ops relevant to shared groups
- [ ] Apply remote ops into local Room with same idempotency rules as backend sync
- [ ] Cursor / watermark per peer+group as needed
- [ ] Conflict handling reuses V1b policy

## Flow

```text
Device A local DB
  → pending SyncOperations
  → PeerTransport
  → Device B applies ops
  → Device B sends its pending ops
  → both local DBs converge
```

## Done when

- [ ] Offline expense on A appears on B over LAN with internet off
- [ ] Re-running exchange does not duplicate expenses
- [ ] Backend sync still works when internet returns later

## Notes

_

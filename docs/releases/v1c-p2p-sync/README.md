# V1c — P2P Sync

**Goal:** Let authorized nearby devices sync group data over local Wi‑Fi / hotspot **without internet**.

**Status:** `NOT_STARTED`  
**Depends on:** V1b `COMPLETED`  
**Started:** —  
**Completed:** —

---

## What ships in V1c

- Nearby device discovery
- Secure local transport
- Exchange of sync operations between authorized devices
- Same logical sync model as backend sync (idempotent ops)
- Invite / join via QR or short code while offline

## What is explicitly NOT in V1c

- Replacing backend sync
- Public internet P2P / DHT
- Broadcasting private expense data during discovery

---

## Phase checklist

| Phase | File | Status |
|---|---|---|
| 01 Discovery & Identity Exchange | [phase-01-discovery.md](phase-01-discovery.md) | `NOT_STARTED` |
| 02 Transport | [phase-02-transport.md](phase-02-transport.md) | `NOT_STARTED` |
| 03 P2P Sync Protocol | [phase-03-p2p-sync-protocol.md](phase-03-p2p-sync-protocol.md) | `NOT_STARTED` |
| 04 P2P Security & Membership | [phase-04-p2p-security.md](phase-04-p2p-security.md) | `NOT_STARTED` |
| 05 Integration Testing | [phase-05-integration-testing.md](phase-05-integration-testing.md) | `NOT_STARTED` |

---

## Exit criteria

- [ ] Two devices on same local network can sync a group offline
- [ ] Discovery shows only minimal profile info
- [ ] Unauthorized device cannot read group expenses
- [ ] Idempotent: re-exchange does not duplicate data
- [ ] Phase 01–05 all `COMPLETED`

When V1c is done, SplitSync V1 feature set for offline + backend + nearby sync is complete.

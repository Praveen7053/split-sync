# V1c Phase 02 — Transport

**Status:** `NOT_STARTED`  
**Started:** —  
**Completed:** —

---

## Objective

Establish a reliable local data channel between two devices after discovery.

---

## Deliverables

- [ ] Transport abstraction (`PeerTransport`) separate from sync logic
- [ ] Connect / disconnect / reconnect handling
- [ ] Message framing (length-prefixed or equivalent)
- [ ] Timeout + failure callbacks
- [ ] Transport unit / device tests for send/receive

## Rule

Sync protocol must not be hard-coded to one socket API — keep transport swappable.

## Done when

- [ ] Two devices can exchange opaque payloads reliably on LAN
- [ ] Disconnects are detected and surfaced cleanly

## Notes

_

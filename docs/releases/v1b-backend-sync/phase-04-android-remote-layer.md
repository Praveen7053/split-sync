# V1b Phase 04 — Android Remote Layer

**Status:** `NOT_STARTED`  
**Started:** —  
**Completed:** —

---

## Objective

Add Android networking without changing the local-first read path.

UI still reads from Room. Remote is for sync / auth only.

---

## Deliverables

- [ ] HTTP client setup (Retrofit/OkHttp or equivalent)
- [ ] Auth interceptor (attach token)
- [ ] Remote DTOs + mappers
- [ ] Remote data sources for API calls
- [ ] Repository updates: local write first, remote via sync layer
- [ ] Configurable backend base URL (debug/local)

## Done when

- [ ] Android can register/login against local backend
- [ ] Offline app behavior from V1a still works with network off
- [ ] No UI screen depends on live network for local data display

## Notes

_

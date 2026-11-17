# V1b Phase 03 — Auth & Account Linking

**Status:** `NOT_STARTED`  
**Started:** —  
**Completed:** —

---

## Objective

Add online authentication without breaking offline local profiles.

**Locked auth for V1b:** Email + password.

---

## Deliverables

- [ ] Register / login endpoints
- [ ] Secure password hashing
- [ ] Token-based API auth (e.g. JWT or opaque token — pick one and document)
- [ ] Link local `userId` to backend account (no ID rewrite of expenses/groups)
- [ ] Group-level authorization on protected endpoints
- [ ] Android login / register / link screens or flows

## Rules

- Local offline profile still works without account
- Linking must not regenerate existing local entity IDs
- Unauthorized group access must fail server-side

## Done when

- [ ] Register → login → call protected API succeeds
- [ ] Unauthenticated protected calls fail
- [ ] Local profile can be linked once and remain stable

## Notes

_

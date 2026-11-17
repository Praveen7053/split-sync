# SplitSync

Offline-first group expense management for Android, with optional backend sync and nearby P2P sync.

## Releases (follow this)

Start here: [`docs/releases/README.md`](docs/releases/README.md)

| Release | Folder | Goal |
|---|---|---|
| V1a | [`docs/releases/v1a-offline-core`](docs/releases/v1a-offline-core) | Offline expense app (no backend) |
| V1b | [`docs/releases/v1b-backend-sync`](docs/releases/v1b-backend-sync) | Backend API + auth + sync |
| V1c | [`docs/releases/v1c-p2p-sync`](docs/releases/v1c-p2p-sync) | Nearby device sync |

Deep design docs live under [`docs/architecture`](docs/architecture).

## Project modules

- `:android:app` — Android client (Java)
- `:backend` — Spring Boot API (Java)

## Current stack locks

- Android: Java, Room/SQLite
- Backend: Spring Boot, JPA, **PostgreSQL**
- API: `/api/v1/`

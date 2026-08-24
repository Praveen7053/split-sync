# SplitSync — Architecture v1

## 1. Product Vision

SplitSync is a general-purpose, offline-first group expense management application designed to help people record, split, track, and settle shared expenses reliably regardless of network availability.

Users can create a local profile, create or join groups, add and manage expenses, calculate balances, and record settlements without requiring an internet connection.

SplitSync is designed to provide a complete experience in both online and offline environments. The application automatically adapts its synchronization behavior based on the connectivity available to the device. Users are not required to manually switch between online and offline modes.

When internet connectivity is available, SplitSync synchronizes local changes with the backend server and authorized devices in the background. When internet connectivity is unavailable, the application continues to operate using locally stored data and queues changes for synchronization when connectivity is restored.

Where supported local connectivity is available, such as a common Wi-Fi network or mobile hotspot, SplitSync may also allow authorized devices to exchange relevant group data without requiring internet access.

A group is the core business concept of the application. A group can represent a trip, family, household, friends, office, event, or any other shared-expense context. A trip is treated as a specialized type of group rather than as a separate expense-management model.

The primary design principle of SplitSync is:

> Local first, sync automatically.

The local device database is the immediate source of truth for the application. Network connectivity is used to synchronize and back up local changes, but network availability must never prevent users from performing core expense-management operations.

SplitSync must provide a consistent and reliable user experience regardless of whether the device has internet connectivity, local network connectivity, or no network connectivity at all.

## 2. Goals

### 2.1 Core Product Goals

- Allow users to create a local profile without requiring an internet connection.
- Allow users to create, join, and manage shared expense groups.
- Support different group contexts such as trips, households, families, friends, offices, and events.
- Allow group members to record expenses and specify who participated in each expense.
- Support multiple expense-splitting methods including equal, exact amount, percentage, and shares.
- Automatically calculate individual balances based on expenses and splits.
- Allow users to record and track settlements between group members.
- Provide a clear overview of total group expenses, individual contributions, shares, balances, and settlements.
- Preserve the complete expense history of a group.
- Provide the same core expense-management capabilities regardless of network availability.

### 2.2 Connectivity and Reliability Goals

- The application must provide a complete and reliable experience both when internet connectivity is available and when it is unavailable.
- The application must automatically detect the available connectivity state and adapt its synchronization behavior without requiring the user to manually switch between online and offline modes.
- The user must not be required to choose between an online mode and an offline mode for normal application usage.
- The local device database must remain the immediate source of truth for the Android application regardless of network availability.
- All core user actions must first be persisted locally before synchronization is attempted.
- When internet connectivity is available, local changes must be synchronized with the backend automatically in the background.
- When internet connectivity is unavailable, local changes must be stored safely and queued for later synchronization.
- Restoring internet connectivity must automatically resume synchronization of pending local changes.
- The application must remain fully usable for its core functionality while synchronization is pending.
- Synchronization must be reliable and idempotent so that the same change cannot create duplicate data.
- Synchronization failures must not prevent users from continuing to use the application locally.
- Locally created or modified data must persist across application restarts.
- Network unavailability or temporary synchronization failures must not result in loss of locally stored data.
- The application must eventually converge toward a consistent state across authorized devices after successful synchronization.
- Internet connectivity and local device-to-device connectivity must be treated as separate capabilities.

### 2.3 Multi-User and Local Communication Goals

- Allow nearby SplitSync users to discover each other over a supported local network without requiring internet connectivity.
- Allow users to exchange the minimum profile information required for identification during local discovery.
- Allow authorized users to establish group membership and exchange relevant group data over a supported local connection without requiring internet connectivity.
- Protect group data so that it is exchanged only between authorized users and devices.
- Support local synchronization between devices when internet connectivity is unavailable but a supported local network is available.
- Ensure that local device-to-device synchronization is an additional synchronization mechanism and does not become a prerequisite for core expense-management functionality.
- Design the synchronization architecture to support groups ranging from a few members to large groups.

### 2.4 Technical Goals

- Use Java for Android application development.
- Use Room with SQLite as the Android local persistence layer.
- Use Java with Spring Boot for the backend.
- Use Hibernate/JPA for backend persistence.
- Use MySQL as the backend database.
- Expose backend functionality through versioned REST APIs.
- Use a dedicated synchronization layer between local device data and remote backend data.
- Keep the Android application functional without requiring direct access to backend services.
- Keep the local data model independent from network availability.
- Maintain clear separation between the domain model, local persistence, network communication, synchronization, and presentation layers.
- Keep the architecture modular so that future features can be added without redesigning the core expense domain.
- Design the synchronization architecture to support both remote server synchronization and future local peer-to-peer synchronization.
- Ensure that online synchronization operates in the background without blocking normal user interactions.
- Ensure that the application can recover safely from temporary network failures, application restarts, and interrupted synchronization operations.
- 
## 3. Non-Goals

## 4. Design Principles

## 5. User Model

## 6. Device Model

## 7. Group Model

## 8. Expense Model

## 9. Expense Split Model

## 10. Settlement Model

## 11. Offline-First Architecture

## 12. Synchronization Architecture

## 13. Conflict Resolution

## 14. Android Architecture

## 15. Backend Architecture

## 16. Database Architecture

## 17. API Architecture

## 18. Security Architecture

## 19. Local Peer-to-Peer Synchronization

## 20. Scalability

## 21. Testing Strategy

## 22. Future Extensions

## 23. Architecture Decisions

## 24. Open Questions